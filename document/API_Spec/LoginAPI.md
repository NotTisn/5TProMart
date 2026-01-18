# 1. LOGIN API

---

## 1.1 Login

**Endpoint:** `POST /api/v1/auth/login`

**Request body**

```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Response 200**

```json
{
  "success": true,
  "message": "Login successfully",
  "data": {
    "accessToken": "ey...", // JWT token
    "expiresIn": 3600,
    "user": {
      "profileId": "string",
      "userId": "string",
      "username": "string",
      "fullName": "string",
      "role": "string"
    }
  }
}
```

**Note** Server set cookie

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

**Endpoint:** `GET /api/v1/auth/me`

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

**Endpoint:** `POST /api/v1/accounts`

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
  "accountType": "string", // Keycloak Role: "Admin" | "Manager" | "SalesStaff" | "WarehouseStaff"
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

**Endpoint:** `PUT /api/v1/auth/change-password`

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

**Endpoint:** `POST /api/v1/auth/logout`

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

## **Note** Server delete cookie refresh_token

## 1.5 Refresh Token

**Endpoint:** `POST /api/v1/auth/refresh-token`

**Header**

Cookie: refresh_token={cookie_string}

**Response 200**

```json
{
  "success": true,
  "message": "Refresh token successfully",
  "data": {
    "accessToken": "string" // New Token
  }
}
```
