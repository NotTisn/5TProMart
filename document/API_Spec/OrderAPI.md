# 1. Orders API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **Base Endpoint** `/api/v1/orders`

## 1.1 Get orders query

**Endpoint:** `GET /api/v1/orders`

**Query Parameters**

```json
{
  "search": "string", // orderId, customerName, customerId
  "staffId": "string",
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy",
  "paymentMethod": "string", // Filter: "Tiền mặt", "Chuyển khoản"
  "status": "string", // Filter: "Đã thanh toán", "Chưa thanh toán", "Đã huỷ"
  "page": 0,
  "size": 10,
  "sort": "orderDate,desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get order list successfully.",
  "data": [
    {
      "orderId": "string",
      "orderDate": "dd-MM-yyyy hh-mm-ss",
      "customerName": "string", // if customerId is null => "Khách lẻ"
      "staffName": "string",
      "totalAmount": number,
      "paymentMethod": "string", // "Tiền mặt", "Chuyển khoản"
      "status": "string",   // "Đã thanh toán", "Chưa thanh toán", "Đã huỷ"
      "createdAt": "dd-MM-yyyy hh-mm-ss"
    }
  ],
  "pagination": {
    "totalItems": 100,
    "totalPages": 10,
    "currentPage": 0,
    "pageSize": 10
  }
}
```

---

---

## 1.2 Get order detail

**Endpoint:** `GET /api/v1/orders/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get order detail successfully.",
  "data": {
    "orderId": "string",
    "orderDate": "10-01-2026 14:30:00",
    "status": "Đã thanh toán",
    "paymentMethod": "Tiền mặt",
    "customer": {
      "customerId": "string",
      "fullName": "string",
      "phoneNumber": "string"
    },
    "staff": {
      "profileId": "string",
      "fullName": "string"
    },
    "items": [
      {
        "productId": "string",
        "productName": "string",
        "quantity": 2,
        "unitPrice": 5000,
        "subTotal": 10000
      }
    ],
    "subTotal": 10000,
    "discountAmount": 0,
    "totalAmount": 10000,
    "amountGiven": 20000,
    "changeReturned": 10000,
    "pointsEarned": 100 //1% hoá đơn?
  }
}
```

---

## 1.3 Check product (scan product code)

**Endpoint**: `POST /api/v1/orders/check-product`

**Request Body**

```json
{
  "lotId": "string", // Mã quét được
  "quantity": 1 // Default : 1
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Item check successfully.",
  "data": {
    "lotId": "string",
    "productId": "string",
    "productName": "string", // Product.productName
    "unitOfMeasure": "string", // productName.unitOfMeasure
    "unitPrice": 10000, // Product.sellingPrice
    "quantity": 2,
    "subTotal": 20000, // unitPrice * quantity
    "currentStock": 50, // StockInventory.stockQuantity
    "status": "string" // StockInventory.status
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Product validation failed.",
  "errors": {
    "lotId": "Lot not found or Expired.",
    "quantity": "Insufficient stock. Current stock lower than quantity."
  }
}
```

---

## 1.4 Create Order (Checkout)

**Endpoint**: `POST /api/v1/orders`

**Request Body**

```json
{
{
  "staffId": "string",
  "customerId": "string", // Nullable
  "paymentMethod": "CASH",
  "amountGiven": 360000,
  "items": [
    { "lotId": "lotId_1", "quantity": 3 },
    { "lotId": "lotId_2", "quantity": 6 }
  ]
}
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Order created",
  "data": {
    "orderId": "string",
    "orderDate": "dd-MM-yyyy hh-mm-ss", //now
    "totalAmount": 300000,
    "changeReturned": 60000, // amount given - total amount
    "items": [
      {
        "productName": "product_name_1",
        "lotId": "lot_id_1",
        "quantity": 3,
        "subTotal": 300000
      },
      {
        "productName": "product_name_2",
        "lotId": "lot_id_2",
        "quantity": 6,
        "subTotal": 60000
      }
    ]
  }
}
```
