# 1. Staff API

**_ Information _**

**_ Header And Required _**

```json
"Authorization": "Bearer {token}",
"role": {
    "name": "Admin"
}
```

## **_ Endpoint base _** `/api/staffs`

## 1.1 Get supplier query

**Endpoint:** `GET /api/staffs/`

**Query Parameters**

```json
{
  "search": "string",       // fullName, phoneNumber, userId contains search string
  "accountType": "string",  // Filter: "SalesStaff", "WarehouseStaff", = chuc vu nhan vien
  "sortBy": "fullName",
  "order": "asc" "desc"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Get staff list successfully.",
  "data": [
    {
      "profileId": "string", // profiles.id
      "userId": "string", // profiles.user_id
      "username": "string", // profiles.username
      "fullName": "string", // profiles.full_name
      "email": "string", // profiles.email
      "phoneNumber": "string", // profiles.phone_number
      "dateOfBirth": "DD/MM/YYY", // profiles.date_of_birth
      "accountType": "string", // profiles.account_type
      "avatarUrl": "string", // profiles.avatar_url
      "location": "string" // profiles.location
    }
  ],
  "pagination": {
    "totalItems": 90,
    "itemsPerPage": 10,
    "totalPages": 10,
    "startPage": 1
  }
}
```

---

## 1.2 Get supplier by id

**Endpoint:** `GET /api/staffs/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Get supplier detail successfully.",
  "data": {
    "profileId": "string", // profiles.id
    "userId": "string", // profiles.user_id
    "username": "string", // profiles.username
    "fullName": "string", // profiles.full_name
    "email": "string", // profiles.email
    "phoneNumber": "string", // profiles.phone_number
    "dateOfBirth": "DD/MM/YYY", // profiles.date_of_birth
    "accountType": "string", // profiles.account_type
    "avatarUrl": "string", // profiles.avatar_url
    "location": "string" // profiles.location
  }
}
```

**Response 404**

```json
{
  "success": false,
  "message": "Staff profile not found.",
  "data": null
}
```

---

## 1.3 Add new staff

//Nghiệp vụ: Admin tạo nhân viên mới, hệ thống sẽ tạo profile + user cùng lúc

**Endpoint**: `POST /api/staffs`

**Request Body**

```json
{
  "username": "string", // Required, Unique
  "password": "string", // Required
  "fullName": "string", // Required
  "email": "string", // Required, Unique
  "phoneNumber": "string", // Required
  "accountType": "string", // "Sales Staff", "Warehouse Staff"
  "dateOfBirth": "DD-MM-YYYY",
  "location": "string",
  "bio": "string"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Staff created successfully.",
  "data": {
    "profileId": "string",
    "username": "string",
    "fullName": "string",
    "accountType": "string",
    "email": "string"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "username": "Username already exists."
  }
}
```

---

## 1.4 Update a staff

**Endpoint**: `PUT /api/staffs/{id}`

**Request Body**

```json
{
  "fullName": "string",
  "email": "string",
  "phoneNumber": "string",
  "accountType": "string",
  "dateOfBirth": "DD-MM-YYYY",
  "location": "string",
  "bio": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Staff updated successfully.",
  "data": {
    "profileId": "string",
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "accountType": "string",
    "dateOfBirth": "DD-MM-YYYY",
    "location": "string",
    "bio": "string"
  }
}
```

---

## 1.5 Delete a staffs

**Endpoint**: `DELETE /api/staffs/{id}`

**Response 200**

```json
{
  "success": true,
  "message": "Staff deleted successfully.",
  "data": null
}
```

---

**Response 409**
//Check không thể xoá nhân viên đang có đơn bán nháp

```json
{
  "success": false,
  "message": "Cannot delete staff.",
  "errors": {
    "profileId": "This staff is currently import/selling orders."
  }
}
```
