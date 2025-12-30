# 2. ProductCategory API

## 2.1 Get all product categories

**Endpoint:** `GET /api/product-categories/`

**Query Parameters**

```json
{
  "search": "string" //categoryId or categoryName contains search string
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get categories successfully.",
  "data": [
    {
      "categoryId": "string",
      "categoryName": "string"
    }
  ]
}
```

## 2.2 Get all product categories

**Endpoint:** `GET /api/product-categories/{id}`
**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "categoryId": "string", //id = category id
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
    "categoryId": "string",
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
    "categoryId": "string",
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
