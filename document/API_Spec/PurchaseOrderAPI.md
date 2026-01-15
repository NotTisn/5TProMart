# 1. Purchase Order API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Warehouse Staff" || "Admin"
}
```

## **Base Endpoint** `/api/v1/purchase_orders`

---

## 2. Get purchase orders list

**Endpoint:** `GET /api/v1/purchase_orders`

**Query Parameters**

```json
{
  "search": "string", // Filter by poId or supplierName
  "status": "string", // Filter: "Draft", "Completed", "Cancelled"
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy", // filter purchaseDate in startDate -> endDate
  "page": 0,
  "size": 10,
  "sortBy": "purchaseDate",
  "order": "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get list successfully.",
  "data": [
    {
      "id": " ",
      "poCode": " ",
      "supplierName": " ",
      "staffNameCreated": " ",
      "totalAmount": 360000,
      "status": " ",
      "purchaseDate": " ",
      "checkDate": "  "
    }
  ],
  "pagination": {
    "totalItems": 50,
    "totalPages": 5,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

---

## 3. Get purchase order detail

**Endpoint:** `GET /api/v1/purchase_orders/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get detail successfully.",
  "data": {
    "_id": "  ",
    "poCode": " ",
    "status": " ",
    "notes": "  ",
    "supplier": {
      "supplierId": " ",
      "supplierName": " ",
      "phone": " ",
      "representName": "  ",
      "representPhoneNumber": " "
    },
    "staffIdCreated": " ",
    "purchaseDate": "  ",
    "staffIdChecked": " ",
    "checkDate": "  ",
    "items": [
      {
        "productId": " ",
        "productName": "  ",
        "importPrice": 3600,
        "quantityOrdered": 10,
        "quantityReceived": 0, //default = 0 when create draft
        "subTotal": 36000
      }
    ],
    "totalAmount": 1000000,
    "generatedLotIds": ["lotId_1", "lotId_2"] // danh sach cac lot_id trong don hang
  }
}
```

---

## 4. Create Draft Purchase Order

**Endpoint**: `POST /api/v1/purchase_orders`

**Request Body**

```json
{
  "supplierId": "",
  "notes": "Don nhap...",
  "items": [
    {
      "productId": "productId_1",
      "quantityOrdered": 18
    },
    {
      "productId": "productId_2",
      "quantityOrdered": 36
    }
  ]
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Created draft purchase orders.",
  "data": {
    "id": "new object id",
    "poId": "new purchase order id",
    "supplierName": " ",
    "status": "Draft", // Default
    "purchaseDate": "now"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Not validate data.",
  "errors": {
    "supplierId": "Supplier is required.",
    "items": "Product list can't empty",
    "items[0].quantityOrdered": "Product quantity must be greater than 0."
  }
}
```

---

## 1.4 Confirm Orders And Genarate Lots Id

**Endpoint**: `POST /api/v1/purchase_orders/{id}/confirm`

**Request Body**

```json
{
  "staffIdChecked": "staffId",
  "checkDate": "now",
  "notes": "Hàng đủ, không hư hỏng, ...",
  "invoice": {
        "invoiceNumber": "HD_1234",
        "invoiceDate": "date",
        "images": ["url_1", "url_2", ...]
    },

  "actualItems": [
    {
      "productId": "productId_01",
      "quantityReceived": 18, // Số lượng nhận
      "importPrice": 36000, // Giá trên hoá đơn của nhà cung cấp xuất khi nhận hàng. Có thể lưu ảnh hoá đơn vào db để double check?
      "manufactureDate": "date",
      "expirationDate": "date", // Nhập từ thực tế sản phẩm
      "notes": " " //note nếu cần: hàng hỏng, quantity = 0, giao thiếu hàng,...
    },
    {
      "productId": "productId_02",
      "quantityReceived": 36,
      "importPrice": 20000,
      "manufactureDate": "date",
      "expirationDate": "date"
    }
  ]
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Order confirmed. Stock Inventory updated.",
  "data": {
    "poId": "poId",
    "status": "Completed",
    "checkDate": "now",
    "finalTotalAmount": 909000, // tính theo import price

    // Frontend bật giao diện in tem theo list này
    "lotsToPrint": [
      {
        "lotId": "",
        "productName": "",
        "quantity": 18, // Số tem cần in = số lượng nhập
        "expirationDate": " ",
        "notes": " " //note nếu cần: hàng hỏng, quantity = 0, giao thiếu hàng,...
      },
      {
        "lotId": "  ",
        "productName": "  ",
        "quantity": 36,
        "expirationDate": " "
      }
    ]
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "actualItems": "Received items cannot be empty.",
    "actualItems[0].expirationDate": "Expiration date is required."
  }
}
```

---

## 1.5 Cancel orders

**Endpoint**: `POST /api/v1/purchase_orders/{id}/cancel`

**Request Body**

```json
{
  "staffIdChecked": "staffId",
  "checkDate": "now",
  "cancelNotesReason": "Nhà cung cấp báo hết hàng"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Purchase order cancelled successfully.",
  "data": {
    "poId": " ",
    "poCode": " ",
    "status": "Cancelled", // Draft -> Cancelled
    "cancellationReason": "Hết hàng. Sai Date, ...",
    "checkDate": "now"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Cannot cancel this order.",
  "errors": {
    "status": "Only Draft orders can be cancelled. Current order status is Completed."
  }
}
```

---

## 1.6 Reprint labels

**Endpoint**: `GET /api/v1/purchase_orders/{id}/labels`

**Response 200**

```json
{
  "success": true,
  "message": "Get labels successfully.",
  "data": [
    {
      "lotId": "",
      "productName": "",
      "quantity": 18, // Số tem cần in = số lượng nhập
      "expirationDate": " "
    },
    {
      "lotId": "  ",
      "productName": "  ",
      "quantity": 36,
      "expirationDate": " "
    }
  ]
}
```

> **Kế hoạch nhập hàng**
>
> 1. Tạo đơn nhập hàng: Chọn nhà cung cấp và danh sách sản phẩm muốn nhập (nhập số lượng + số lượng sản phẩm muốn nhập, không nhập date + giá)
> 2. Backend tạo purchase order, status là draft

> **Kiểm hàng**
>
> 1. Nhân viên in đơn nhập nháp trên app để kiểm hàng
> 2. Kiểm tra số lượng, hạn sử dụng, tình trạng,...
> 3. Nếu hàng không nhận được, gọi **1.5 Cancel orders** và nhập lý do

> **Nhập hàng vào kho**
>
> 1. Nhân viên thanh toán, nhận hoá đơn đỏ của nhà cung cấp, nhập thông tin hoá đơn, chụp ảnh
> 2. Với mỗi sản phẩm trong đơn, nhập số lượng nhận, hạn sử dụng, giá nhập (nhập theo hoá đơn của ncc)
> 3. Backend xử lý **3.1 Backend xử lý nhập kho**
> 4. Sau khi nhận response, hiển thị giao diện in tem, in mã vạch theo danh sách lotsToPrint từ API

**3.1 Backend xử lý nhập kho**

> 1. Cập nhật đơn thành Completed
> 2. Gộp lô theo logic: Nếu có lô trùng productId và expirationDate -> cộng dồn, trả về lotId đó. Nếu chưa có, tạo mới với số lượng = số lượng nhập, trả về lotId.
> 3. Trả lotsToPrint theo mẫu: lotId, productName, quantity, expirationDate
> 4. Xem Product API để xử lý số lượng

> **New collection db**

```json
{
  "_id": "ObjectId",
  "poId": "String",
  "supplier": {
    "supplierId": "String",
    "supplierName": "String",
    "phone": "String",
    "representName": "string",
    "representPhoneNumber": "string"
  },
  "staffIdCreated": "String", //Id nhan vien tao phieu
  "staffIdChecked": "String", //Id nhan vien kiem phieu

  "status": "String", // Enum: "Draft" | "Completed" | "Cancelled"
  "notes": "String", // Ghi chu neu co

  "totalAmount": "Number", // Tong tien
  "purchaseDate": "Date", // Ngay tao
  "checkDate": "Date", // Ngay kiem va nhan

  "invoice": {
        "invoiceNumber": "HD_1234",
        "invoiceDate": "date",
        "images": ["url_1", "url_2", ...]
    },

  "items": [
    {
      "productId": "String",
      "productName": "String",
      "importPrice": "Number",
      "quantityOrdered": "Number",
      "quantityReceived": "Number", // So luong nhan ve, update khi check
      "subTotal": "Number" // quantityOrdered * importPrice
    }
  ],

  "generatedLotIds": ["String"]
}
```
