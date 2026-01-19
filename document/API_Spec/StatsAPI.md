# 1. Stats Management

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **_ Endpoint base _** `/api/v1/stats`

## 1.1 Get stats query

**Endpoint:** `GET /api/v1/stats/`

**Query Parameters**

```json
{
  "category": "dd-MM-yyyy",
  "endDate": "dd-MM-yyyy",
  "search": "string", // Filter by category or description
  "sortBy": "payDate",
  "order": "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get expenses successfully.",
  "data": [
    {
      "id": "String",
      "category": "Tiền điện tháng 1/2026",
      "description": "Tiền điện tháng 1/2026. Đã thanh toán",
      "payDate": "15-01-2026",
      "amount": 2500000,
      "image": "Hoá_Đơn.png"
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

## 1.2 Create stats

**Endpoint:** `POST /api/v1/stats/`

**Request Body**

````json
{
  "category": "String",
  "description": "String",
  "amount": 500000,
  "payDate": "20-01-2026",
  "image": "[String]"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Stats created.",
  "data": {
    "id": "String",
    "category": "String",
    "amount": 500000
  }
}
```

````

---

## 1.3 Update stats

**Endpoint:** `PUT /api/v1/stats/{id}`

**Request Body**

````json
{
  "category": "String",
  "description": "String",
  "amount": 500000,
  "payDate": "20-01-2026",
  "image": "[String]"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Stats updated successfully.",
  "data": {
    "category": "String",
    "description": "String",
    "amount": 500000,
    "payDate": "20-01-2026",
    "image": "[String]"
    }
}
```
````

## 1.4 Get Stats report

**Endpoint:** `GET /api/v1/stats/category-report`

**Request Body**

````json
{
  "startDate": "dd-MM-yyyy", // Required
  "endDate": "dd-MM-yyyy"   // Required
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get stats report successfully.",
  "data": {
    "totalAmount": 15000000,
    "breakdown": [
      {
        "categoryName": "Tiền điện",
        "totalAmount": 50000,
      },
      {
        "categoryName": "Tiền nước",
        "totalAmount": 20000,
      }
    ]
  }
}
```
````
