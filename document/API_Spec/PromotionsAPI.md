# 1. Promotionns API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **Base Endpoint** `/api/v1/promotions`

## 1.1 Get promotions query

**Endpoint:** `GET /api/v1/promotions`

**Query Parameters**

```json
{
  "search": "string", // Filter promotionId, promotionName, productName
  "type": "string", // Filter: "Discount", "Buy X Get Y"
  "status": "string", // Filter: "Active", "Expired", "Upcoming", "Canceled"
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy",
  "sortBy": "startDate" || "endDate",
  "order": "asc" || "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get promotions list successfully.",
  "data": [
    {
      "promotionId": "promotionId_01",
      "name": "Khuyen mai Tet",
      "promotionType": "Discount",
      "products": [
        {
          "productId": "productId_01",
          "productName": "Coca"
        },
        {
          "productId": "productId_02",
          "productName": "Pepsi"
        }
      ],
      "discountPercent": 20,
      "startDate": "01-01-2026",
      "endDate": "31-12-2025",
      "status": "Active"
    },
    {
      "promotionId": "promotionId_02",
      "name": "Khuyen mai het Tet",
      "promotionType": "Buy X Get Y",
      "products": [
        {
          "productId": "productId_01",
          "productName": "Coca"
        },
        {
          "productId": "productId_02",
          "productName": "Pepsi"
        }
      ],
      "buyQuantity": 1,
      "getQuantity": 2,
      "startDate": "01-02-2026",
      "endDate": "31-02-2026",
      "status": "Upcoming"
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

## 1.2 Get promotion detail

**Endpoint:** `GET /api/v1/promotions/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get promotions detail successfully.",
  "data": {
    "promotionId": "promotionId_01",
    "promotionName": "Khuyen mai Tet",
    "promotionDescription": "Giam gia nuoc ngot cuc soc, mua ve don Tet",
    "promotionType": "Discount",

    "discountPercent": 20,
    "buyQuantity": null,
    "getQuantity": null,

    "status": "Active",
    "startDate": "01-01-2026",
    "endDate": "31-01-2026",

    "products": [
      {
        "productId": "productId_01",
        "productName": "Coca",
        "unitOfMeasure": "Lon",
        "sellingPrice": 10000,
        "promotionPrice": 8000 //null neu la khuyen mai dang buy x get y, khong can show
      }
    ]
  }
}
```

---

## 1.3 Create Promotion

**Endpoint**: `POST /api/v1/promotions`

**Request Body**

**Discount**

```json
{
  "promotionName": "String",
  "promotionDescription": "String",

  "products": ["productId_01", "productId_02",...],

  "promotionType": "Discount",      // Required
  "discountPercent": 10,            // Required

  "startDate": "Date",
  "endDate": "Date"
}
```

**Buy X Get Y**

```json
{
  "promotionName": "String",
  "promotionDescription": "String",

  "products": ["productId_01", "productId_02",...],

  "promotionType": "Buy X Get Y",      // Required
  "buyQuantity": 1,        // Required
  "getQuantity": 1,        // Required

  "startDate": "Date",
  "endDate": "Date"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Promotion created successfully.",
  "data": {
    "promotionId": "String",
    "promotionName": "String",
    "promotionDescription": "String",
    "status": "Upcoming", //BE chay code kiem tra de gan status vao
    "startDate": "Date",
    "endDate": "Date"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "endDate": "End Date must be after Start Date.",
    "discountPercent": "Discount percent must be between 1 and 100.",
    "buyQuantity": "Buy quantity must be greater than 0",
    "getQuantity": "Get quantity must be greater than 0",
    "conflict": "Product 'productName' already has an active promotion 'promotionId' in this period."
  }
}
```

---

## 1.4 Cancel promtions

**Endpoint**: `PUT /api/v1/promotions/{id}/cancel`

**Response 200**

```json
{
  "success": true,
  "message": "Promotion cancelled.",
  "data": {
    "promotionId": "String",
    "status": "Cancelled"
  }
}
```

> **NOTE LUỒNG:**
>
> **GHI CHÚ**
>
> 1. Khuyến mãi có 2 loại Discount và Buy X Get Y
> 2. Chỉ Discount có cột discountPercent, buyQuantity và getQuantity = null, ngược lại với Buy X Get Y

> **NOTE FOR FRONTEND:**
>
> 1. Khi thêm thì FE cho chọn 1 trong 2 loại, chọn loại nào thì chỉ hiện trường thông tin của loại đó và ẩn trường của loại kia đi

> **NOTE FOR BACKEND:**
>
> 1. Khi thêm khuyến mãi mới, BE phải kiểm tra trong khoảng startDate -> endDate các sản phẩm trong danh sách đã có khuyến mãi nào Active chưa, nếu có thì response conflict

> **NOTE CHO TÍNH NĂNG BÁN HÀNG (ORDERS)**
>
> 1. Khi gọi API **1.3 Check product (scan product code)**, hệ thống phải:
> 2. Query để xem Promotion đang có status "Active" của sản phẩm đó
> 3. Xử lý giá với "Discount" và tự thêm sản phẩm giá 0đ với "Buy X Get Y" khi khách mua >= X

> **PROMOTION COLLECTION**

```json
{
  "_id": "ObjectId",
  "promotionId": "string",
  "promotionName": "string",
  "promotionDescription": "string",
  "products": [
    {
      "productId": "String", // Ref Product
      "productName": "String" // Cache product name
    }
  ],

  "promotionType": "string", // "Discount" || "Buy X Get Y",
  "discountPercent": "Number", // tinh bang %

  "buyQuantity": "Number",
  "getQuantity": "Number",

  "startDate": "Date",
  "endDate": "Date",

  "status": "String", // "Active" | "Upcoming" | "Expired" | "Cancelled"

  "createdAt": "Date",
  "updatedAt": "Date"
}
```
