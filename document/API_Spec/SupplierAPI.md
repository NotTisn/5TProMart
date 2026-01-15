# 1. Supplier API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Warehouse Staff" || "Admin"
}
```

## **Endpoint base:** `/api/v1/suppliers`

## 1.1 Get supplier query

**Endpoint:** `GET /api/v1/suppliers/`

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

## 1.2 Get supplier by id

**Endpoint:** `GET /api/v1/suppliers/{id}`

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
    "currentDebt": "number"
  }
}
```

---

## 1.3 Add new supplier

**Endpoint**: `POST /api/v1/suppliers`

**Request Body**

```json
{
  "supplierName": "string", // required
  "phoneNumber": "string", // required
  "address": "string", // required
  "representName": "string", // not required
  "representPhoneNumber": "string", // not required
  "supplierType": "string", // "Doanh nghiệp" || "Tư nhân"
  "suppliedProductType": ["productId_01", "productId_02, ..."] //Danh sach productId cung cap
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
    "suppliedProductType": [
      { "productId": "productId_01", "lastImportPrice": 0, "lastImportDate": null },
      { "productId": "productId_02", "lastImportPrice": 0, "lastImportDate": null },
      {...}
    ],
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

**Endpoint**: `PUT /api/v1/supplier/{id}`

**Request Body**

```json
{
  "supplierName": "string",
  "phoneNumber": "string",
  "address": "string",
  "representName": "string",
  "representPhoneNumber": "string",
  "supplierType": "string",
  "suppliedProductType": ["productId_01", "productId_03, ..."] //Danh sach productId moi
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Supplier updated successfully.",
  "data": {
    "supplierName": "string",
    "phoneNumber": "string",
    "address": "string",
    "representName": "string",
    "representPhoneNumber": "string",
    "supplierType": "string",
    "suppliedProductType": [
      { "productId": "productId_01", "lastImportPrice": 100000, "lastImportDate": "15/01/2025" },   // Giu nguyen neu la san pham cu
      { "productId": "productId_03", "lastImportPrice": 0, "lastImportDate": null },  // Tao default value neu la san pham moi
      {...}
    ],
    "currentDebt": "number" //not change
  }
}
```

**Response 400**
Same 1.3

---

## 1.5 Delete a supplier

**Endpoint**: `DELETE /api/v1/supplier/{id}`

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

---

## 1.6 Get supplier's products

**Endpoint**: `GET /api/v1/suppliers/{id}/products`

**Response 201**

```json
{
  "success": true,
  "message": "Get supplier products successfully.",
  "data": [
    {
      "productId": "productId_01",
      "productName": "productName_01",
      "unitOfMeasure": "Lo",
      "totalStockQuantity": 36,

      // Get from field Supplier.suppliedProducts theo ProductId, gop vao du lieu tren va response ve cho UI
      "lastImportPrice": 1000,
      "lastImportDate": "15/01/2026"
    }
  ],
  "pagination": {
    "totalItems": 50,
    "itemsPerPage": 10,
    "totalPages": 5,
    "currentPage": 0
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Not found supplier."
}
```

**FE Note**

> 1. Show totalQuantity, lastImportPrice, lastImportDate
> 2. Neu lastImportDate = null -> chưa nhập lần nào -> hiển thị trống
> 3. Trong menu nhập giá khi confirm nhập hàng có thể tự đồng điền sẵn lastImportPrice cho nhanh
