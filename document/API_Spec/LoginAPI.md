# 1. LOGIN API

---

## 1.1 Login

**Endpoint:** `POST /api/auth/login`

**Request body**

```json
{
  "username": "string",
  "password": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Login successfully",
  "data": {
    "token": "string", // JWT Token
    "expiresIn": 3600,
    "user": {
      "profileId": "string", // profiles.id
      "userId": "string", // profiles.user_id (ID tài khoản gốc)
      "username": "string", // profiles.username
      "fullName": "string", // profiles.full_name
      "email": "string", // profiles.email
      "avatarUrl": "string", // profiles.avatar_url
      "accountType": "string" // profiles.account_type (role user)
    }
  }
}
```

---

**Response 401**

```json
{
  "success": false,
  "message": "Invalid username or password.",
  "data": null
}
```

---

## 1.2 Get current user

**Endpoint:** `GET /api/auth/me`

**Header**

Authorization: Bearer {token}

**Response 200**

```json
{
  "success": true,
  "message": "Get profile successfully.",
  "data": {
    "profileId": "string",
    "userId": "string",
    "username": "string",
    "fullName": "string",
    "email": "string",
    "phoneNumber": "string",
    "dateOfBirth": "DD-MM-YYYY",
    "location": "string",
    "bio": "string",
    "accountType": "string",
    "avatarUrl": "string"
  }
}
```

---

## 1.3 Create staff account

**Endpoint:** `POST /api/accounts`

**Header**

Authorization: Bearer {token}

Required:

```json
"role": {
    "name": "Admin"
}
```

---

**Request Body**

```json
{
  "username": "string", // Required, Unique
  "password": "string", // Required
  "fullName": "string", // Required
  "email": "string", // Required, Unique
  "phoneNumber": "string",
  "accountType": "string", // "Admin" || "SalesStaff" || "WarehouseStaff"
  "dateOfBirth": "DD-MM-YYYY"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Account created successfully.",
  "data": {
    "profileId": "string",
    "username": "string",
    "fullName": "string",
    "accountType": "string"
  }
}
```

**Response 400**

```json
{
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "username": "Username already exists.",
    "email": "Email already used by another account."
  }
}
}
```

---

## 1.4 Change password

**Endpoint:** `PUT /api/auth/change-password`

**Header**

Authorization: Bearer {token}

**Request Body**

```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Password changed successfully."
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "currentPassword": "Incorrect password."
  }
}
```

---

## 1.5 Logout

**Endpoint:** `PUT /api/auth/change-password`

**Header**

Authorization: Bearer {token}

**Response 200**

```json
{
  "success": true,
  "message": "Logged out successfully.",
  "data": null
}
```
