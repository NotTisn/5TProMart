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
  "includeDeleted": false, // true: Show deleted promotions, false: Only active (default)
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
          "productBuy": "productId_01",
          "productName": "Coca",
          "productGet": "productId_02",
          "productName": "Kho Ga"
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

**Discount Percent**

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

**Buy X Get Y**

```json
{
  "success": true,
  "message": "Get promotions detail successfully.",
  "data": {
    "promotionId": "promotionId_02",
    "promotionName": "Khuyen mai Tet",
    "promotionDescription": "Giam gia nuoc ngot cuc soc, mua ve don Tet",
    "promotionType": "Discount",

    "products": [
      {
        "productBuy": "productId_01",
        "productName": "Coca",
        "productGet": "productId_02",
        "productName": "Kho Ga"
      }
    ],
    "discountPercent": 0,
    "buyQuantity": 1,
    "getQuantity": 1,

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

  "products": [
    {
      "productBuy": "productId_01",
      "productGet": "productId_02"
    },
    {
      "productBuy": "productId_03",
      "productGet": "productId_04"
    }
  ],

  "promotionType": "Buy X Get Y", // Required
  "buyQuantity": 1, // Required
  "getQuantity": 1, // Required

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

---

## 1.5 Update promtions

**Endpoint**: `PUT /api/v1/promotions/{id}/cancel`

**Request Body**

```json
{
  "promotionName": "String",
  "promotionDescription": "Stringz",
  "products": ["productId_01", "productId_03"],
  "discountPercent": 15,
  "startDate": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Promotion updated successfully.",
  "data": {
    "promotionId": "promotionId_01",
    "status": "Active", // BE tính và set lại
    "updatedAt": "2026-01-20T..."
  }
}
```

---

## 1.6 Delete Promotion (Soft Delete)

**Endpoint**: `DELETE /api/v1/promotions/{id}`

**Authorization**: Admin only

**Description**: Soft delete a promotion by setting `isActive = false`. The promotion is not physically removed from the database.

**Path Parameters**:

- `id` (string, required): Promotion ID to soft delete

**Response 200**

```json
{
  "success": true,
  "message": "Promotion deleted successfully.",
  "data": {
    "promotionId": "promotionId_01",
    "isActive": false,
    "updatedAt": "2026-01-20T10:30:00Z"
  }
}
```

**Response 404**

```json
{
  "success": false,
  "message": "Promotion not found.",
  "errors": {
    "promotionId": "Promotion with ID 'promotionId_01' does not exist."
  }
}
```

**Response 403**

```json
{
  "success": false,
  "message": "Access denied.",
  "errors": {
    "authorization": "Only Admin users can delete promotions."
  }
}
```

---

## 1.7 Restore Promotion

**Endpoint**: `POST /api/v1/promotions/{id}/restore`

**Authorization**: Admin only

**Description**: Restore a soft-deleted promotion by setting `isActive = true`.

**Path Parameters**:

- `id` (string, required): Promotion ID to restore

**Response 200**

```json
{
  "success": true,
  "message": "Promotion restored successfully.",
  "data": {
    "promotionId": "promotionId_01",
    "promotionName": "Tet 2026 Discount",
    "isActive": true,
    "status": "Active",
    "updatedAt": "2026-01-20T11:00:00Z"
  }
}
```

**Response 404**

```json
{
  "success": false,
  "message": "Promotion not found.",
  "errors": {
    "promotionId": "Promotion with ID 'promotionId_01' does not exist."
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Promotion is already active.",
  "errors": {
    "promotionId": "Promotion 'promotionId_01' is not deleted."
  }
}
```

**Response 403**

```json
{
  "success": false,
  "message": "Access denied.",
  "errors": {
    "authorization": "Only Admin users can restore promotions."
  }
}
```

---

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
> 2. Chỉ được update khuyến mãi có trạng thái khác "Active"

> **NOTE CHO TÍNH NĂNG BÁN HÀNG (ORDERS)**
>
> 1. Khi gọi API **1.3 Check product (scan product code)**, hệ thống phải:
> 2. Query để xem Promotion đang có status "Active" của sản phẩm đó
> 3. Xử lý giá với "Discount" và tự thêm sản phẩm "getProduct" số lượng "getQuantity" với giá 0đ khi khi khách mua "getProduct" >= "buyQuantity"

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
