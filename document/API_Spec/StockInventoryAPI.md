# 5. StockInventory API

## 5.1 Get all stock inventory query

//Not use import day

**Endpoint:** `GET /api/stock_inventories`

**Query Parameters**

```json
{
    "search": "string",      //lot_id contain search string
    "productId": "string",   //filter product_id
    "status": "string",     //filter status
    "sortBy": "expirationDate" "stockQuantity" "importPrice", // Sort by...
    "order": "asc" "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "lotId": "string",
      "productId": "string",
      "productName": "string", //ref table Product -> productId
      "manufactureDate": "dd-MM-yyyy",
      "expirationDate": "dd-MM-yyyy",
      "stockQuantity": "number",
      "importPrice": "number",
      "quantityShelf": "number",
      "quantityStorage": "number",
      "status": "string"
    }
  ],
  "pagination": {
    "totalItems": "number",
    "itemsPerPage": "number",
    "totalPages": "number",
    "startPage": 1
  }
}
```

---

## 5.2 Get stock inventory by id

**Endpoint:** `GET /api/stock_inventories/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "lotId": "string",
    "productId": "string",
    "manufactureDate": "dd-MM-yyyy",
    "expirationDate": "dd-MM-yyyy",
    "stockQuantity": "number",
    "importPrice": "number",
    "quantityShelf": "number",
    "quantityStorage": "number",
    "status": "string"
  }
}
```

---

## 5.3 Add new stock inventory (use when add a new product)

**Endpoint:** `POST /api/stock_inventories`

**Request Body**

```json
{
  "productId": "string",
  "manufactureDate": "dd-MM-yyyy",
  "expirationDate": "dd-MM-yyyy",
  "stockQuantity": "number",
  "importPrice": "number"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "lotId": "string",
    "productId": "string",
    "manufactureDate": "dd-MM-yyyy",
    "expirationDate": "dd-MM-yyyy",
    "stockQuantity": "number",
    "importPrice": "number",
    "quantityShelf": 0, // default = 0
    "quantityStorage": "number", // quantityStorage = stockQuantity
    "status": ""
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "productId": "product id is required",
    "ManuExpDate": "Manufacture Date must before Expiration Date",
    "stockQuantity": "Stock quantity must greater than 0",
    "status": "No status found"
  }
}
```

---

## 5.4 Update stock inventory

**Endpoint:** `PUT /api/stock-inventories/{lot_id}`

**Request Body**

```json
{
  "stockQuantity": "number",
  "status": "string",
  "quantityStorage": "number",
  "quantityShelf": "number"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Stock inventory updated.",
  "data": {
    "lotId": "string",
    "stockQuantity": "number",
    "quantityStorage": "number",
    "quantityShelf": "number",
    "status": "string"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "stockQuantity": "Stock quantity must greater than 0",
    "status": "No status found"
  }
}
```

## 5.5 Disposal Stock Inventory

**Endpoint:** `POST /api/v1/inventory/disposal`

**Request Body**

```json
{
  "reason": "String",
  "note": "String",
  "items": [
    {
      "lotId": "lotId_01",
      "quantity": 5
    },
    {
      "lotId": "lotId_02",
      "quantity": 10
    }
  ],
  "image": ["url_image",...]    // Hình ảnh huỷ
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Disposal created successfully.",
  "data": {
    "disposalId": "String",
    "staffId": "String",
    "Date": "21-01-2026 12:00:00", // now
    "totalItems": 15 // tổng các quantity
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "items[0].quantity": "Disposal quantity cannot be greater than current stock of lot 'lotId'."
  }
}
```

**New Collection**

**Product Disposal**

```json
{
  "id": "String",
  "disposalId": "String",
  "staffId": "ObjectId", // StaffId
  "reason": "String", // Lý do huỷ
  "note": "String", // Ghi chú chi tiết
  "items": [
    {
      "lotId": "ObjectId", // Ref Stock Inventory
      "productId": "String", // Cache sản phẩm
      "productName": "ObjectId",
      "quantity": "Number", // Số lượng huỷ
      "costPrice": "Number" // Lấy importPrice theo lotId * quantity
    }
  ],
  "totalLossValue": "Number", // Tổng giá trị các costPrice để tính thiệt hại tiền hàng
  "Date": "Date"
}
```

**Note xử lý**

> 1. Tạo bản ghi mới trong **Product Disposal**.

> 2. Tìm Id của lô hàng và trừ tồn kho

> 3. Trừ tồn kho cache **totalStockQuantity** trên bảng **Product** cho khớp

> Trước khi trừ, phải kiểm tra số lượng tồn hiện tại. Nếu số lượng muốn huỷ > số lượng tồn thực tế -> Trả về 400.

**Note xử lý quantityStorage và quantityShelf**

> 1. quantityStorage + quantityShelf = stockQuantity

> 2. Khi nhập hàng mới, đặt quantityStorage = stockQuantity, quantityShelf = 0

> 3. Khi lấy hàng từ kho ra trưng bày, update quantityShelf += số lượng lấy ra, trừ quantityStorage
