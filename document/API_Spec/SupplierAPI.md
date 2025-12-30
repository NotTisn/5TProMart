# 1. Supplier API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **Endpoint base:** `/api/suppliers`

## 1.1 Get supplier query

**Endpoint:** `GET /api/suppliers/`

**Query Parameters**

```json
{
  "search": "string",       // supplier_name || supllier_id contains search string
  "supplierType": "string", // Filter supplier type
  "sortBy": "supplierName" || "supplierId" || "currentDebt",
  "order": "asc" "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get suppliers list successfully.",
  "data": [
    {
      "supplierId": "string",
      "supplierName": "string",
      "address": "string",
      "phoneNumber": "string",
      "representName": "string",
      "representPhoneNumber": "string",
      "supplierType": "string",
      "suppliedProductType": "string",
      "currentDebt": "number"
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

---

## 1.2 Get supplier by id

**Endpoint:** `GET /api/supplier/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get supplier detail successfully.",
  "data": {
    "supplierId": "string",
    "supplierName": "string",
    "address": "string",
    "phoneNumber": "string",
    "representName": "string",
    "representPhoneNumber": "string",
    "supplierType": "string",
    "suppliedProductType": "string",
    "currentDebt": "number"
  }
}
```

---

## 1.3 Add new supplier

**Endpoint**: `POST /api/supplier`

**Request Body**

```json
{
  "supplierName": "string", // required
  "phoneNumber": "string", // required
  "address": "string", // required
  "representName": "string", // not required
  "representPhoneNumber": "string", // not required
  "supplierType": "string", // "Doanh nghiệp" || "Tư nhân"
  "suppliedProductType": "string" // Ref from product category name
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Supplier created successfully.",
  "data": {
    "supplierId": "string",
    "supplierName": "string",
    "phoneNumber": "string",
    "address": "string",
    "representName": "string",
    "representPhoneNumber": "string",
    "supplierType": "string",
    "suppliedProductType": "string",
    "currentDebt": 0 // Default value
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "supplierName": "Supplier name is required.",
    "phoneNumber": [
      {
        "code": "REQUIRED",
        "message": "Phone number is required."
      },
      {
        "code": "INVALID_VALUE",
        "message": "Phone number must have 10 digits."
      }
    ],
    "address": "Address is required."
  }
}
```

---

## 1.4 Update a supplier

**Endpoint**: `PUT /api/supplier/{id}`

**Request Body**

```json
{
  "supplierName": "string",
  "phoneNumber": "string",
  "address": "string",
  "representName": "string",
  "representPhoneNumber": "string",
  "supplierType": "string",
  "suppliedProductType": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Supplier created successfully.",
  "data": {
    "supplierName": "string",
    "phoneNumber": "string",
    "address": "string",
    "representName": "string",
    "representPhoneNumber": "string",
    "supplierType": "string",
    "suppliedProductType": "string",
    "currentDebt": "number" //not change
  }
}
```

**Response 400**
Same 1.3

---

## 1.5 Delete a supplier

**Endpoint**: `DELETE /api/supplier/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": null
}
```

---

**Response 400**

```json
{
  "success": false,
  "message": "Cannot delete supplier.",
  "errors": {
    "currentDebt": "Supplier has import product history. Cannot delete."
  }
}
```

---

**Response 409**

```json
{
  "success": false,
  "message": "Cannot delete supplier.",
  "errors": {
    "currentDebt": "This supplier has an outstanding debt of {currentDebt.value}. Cannot delete"
  }
}
```
