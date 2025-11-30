# 2. ProductCategory API

## 2.1 Get all product categories

**Endpoint:** `GET /api/customers/`

**Query Parameters**

```json
{
  "id": "string", //category id = id
  "categoryName": "string", //contains
  "sortBy": "categoryName", // Sort by...
  "order": "desc" //Sort danh sach san pham tu a->z de tim
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "categoryId": "number",
      "categoryName": "string"
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

## 2.2 Get all product categories

**Endpoint:** `GET /api/customers/{id}`
**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "categoryId": "number", //id = category id
    "categoryName": "string"
  }
}
```

## 2.3 Add new product category

**Endpoint:** `POST /api/product-categories`

**Request body**

```json
{
  "categoryName": "string"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "categoryId": "number",
    "categoryName": "string"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "categoryName": "Catogory name is required."
  }
}
```

---

## 2.4 Update a product category

**Endpoint:** `PUT /api/product-categories/{id}`

**Request body**

```json
{
  "categoryName": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "categoryId": "number",
    "categoryName": "string"
  }
}
```

**Response 400**
Same 2.3

---

## 2.5 Delete a product category

**Endpoint:** `DELETE /api/product-categories/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": null
}
```

---
