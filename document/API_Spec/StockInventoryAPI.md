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
{
  "stockQuantity": "number",
  "status": "string"
}
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
