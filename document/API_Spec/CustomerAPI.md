# 1. Customer API

---

## 1.1 Get customers query

**Endpoint:** `GET /api/customers`

**Query Parameters**

```json
{
  "id": "string",   //customer id = id
  "fullName": "string",  //fullName contains value
  "sortBy": "loyaltyPoints",  // Sort by...
  "order": "asc" "desc"   // asc: tang, desc: giam
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": [
    {
      "customerId": "string",
      "fullName": "string",
      "gender": "string",
      "dateOfBirth": "DD-MM-YYYY",
      "phoneNumber": "string",
      "registrationDate": "DD-MM-YYYY",
      "loyaltyPoints": "number"
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

## 1.2 Get customers by id

**Endpoint:** `GET /api/customers/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "customerId": "string", //id = customerId
    "fullName": "string",
    "gender": "string",
    "dateOfBirth": "DD-MM-YYYY",
    "phoneNumber": "string",
    "registrationDate": "DD-MM-YYYY",
    "loyaltyPoints": "number"
  }
}
```

**Response 404**

```json
{
{
  "success": false,
  "message": "Customer with ID {customerId} not found",
  "errors": null
}
}
```

---

## 1.3 Add new customer

**Endpoint**: `POST /api/customers`

**Request Body**

```json
{
  "fullName": "string",
  "gender": "string", // Required: Male, Female, Other
  "dateOfBirth": "01-01-2000", // Format: dd-MM-yyyy
  "phoneNumber": "string"
}
```

**Response 201**

```json
{
  "success": true,
  "statusCode": 201,
  "message": "Customer created successfully",
  "data": {
    "customerId": "string",
    "fullName": "string",
    "registrationDate": "dd-MM-yyyy", //today
    "loyaltyPoints": 0
  }
}
```

**Response 400**

```json
{
  "success": false,
  "statusCode": 400,
  "message": "Validation failed.",
  "errors": {
    "fullName": ["Full name is required."],
    "dateOfBirth": ["Date of birth must be in the past."],
    "phoneNumber": ["Phone number must have 10 digits."]
  }
}
```

---

## 1.4 Update a customer

**Endpoint**: `PUT /api/customers/{id}`

**Request Body**

```json
{
  "fullName": "string",
  "gender": "string",
  "dateOfBirth": "DD-MM-YYYY",
  "phoneNumber": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "string",
  "data": {
    "customerId": "string",
    "fullName": "string",
    "gender": "string",
    "dateOfBirth": "DD-MM-YYYY",
    "phoneNumber": "string",
    "registrationDate": "DD-MM-YYYY",
    "loyaltyPoints": "number"
  }
}
```

**Response 400**
Same 1.3

---

## 1.5 Delete a customer

**Endpoint**: `DELETE /api/customers/{id}`

**Response 200**

```json
{
  "success": true,
  "statusCode": 200,
  "message": "Customer deleted successfully",
  "data": null
}
```

---
