# 3. Product API

---

## 3.1 Get all products query

**Endpoint:** `GET /api/products`

**Query Parameters**

```json
{
  "search": "string",     // productId or productName contains search string
  "categoryId": "string",  //category id = category id, filter category
  "product name": "string",   //contains
  "sortBy": "productName" "unitOfMeasure" "sellingPrice" , // Sort by...
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
      "productId": "string",
      "productName": "string",
      "categoryId": "number",
      "categoryName": "string", //ref categoryId from category table, display on UI
      "unitOfMeasure": "string",
      "sellingPrice": "number"
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

## 3.2 Get product by id

**Endpoint:** `GET /api/products/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "productId": "string", //product Id = id
    "productName": "string",
    "categoryId": "number",
    "unitOfMeasure": "string",
    "sellingPrice": "number"
  }
}
```

---

## 3.3 Add a product

**Endpoint:** `POST /api/products`

**Request Body**

```json
{
  "productName": "string",
  "categoryId": "number",
  "unitOfMeasure": "string",
  "sellingPrice": "number"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "productId": "string",
    "productName": "string",
    "categoryId": "number",
    "unitOfMeasure": "string",
    "sellingPrice": "number"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "productName": "Product name is required",
    "categoryId": "Category not found",
    "unitOfMeasure": [
      {
        "code": "REQUIRED",
        "message": "Unit of measure is required."
      },
      {
        "code": "INVALID_VALUE",
        "message": "Unit of measure must be greater than 0."
      }
    ],
    "sellingPrice": [
      {
        "code": "REQUIRED",
        "message": "Unit of measure is required."
      },
      {
        "code": "INVALID_VALUE",
        "message": "Selling price must be greater than 1000."
      }
    ]
  }
}
```

---

## 3.4 Update a product

**Endpoint:** `PUT /api/products/{id}`

**Request Body**

```json
{
  "productName": "string",
  "categoryId": "number",
  "unitOfMeasure": "string",
  "sellingPrice": "number"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "productId": "string",
    "productName": "string",
    "categoryId": "number",
    "unitOfMeasure": "string",
    "sellingPrice": "number"
  }
}
```

**Response 400**
Same 400 3.3

```json
{
  "success": true,
  "message": "string",
  "data": {
    "productId": "string",
    "productName": "string",
    "categoryId": "number",
    "unitOfMeasure": "string",
    "sellingPrice": "number"
  }
}
```

---

## 3.5 Delete a product

**Endpoint:** `DELETE /api/products/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": null
}
```

---
