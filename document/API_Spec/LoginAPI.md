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
  "message": "Login successful.",
  "data": {
    "token": "string",
    "expiresIn": 3600,
    "user": {
      "username": "string",
      "employeeId": "string",
      "fullName": "string", // Ref employeeId from Emlopyee table
      "role": {
        "id": "number",
        "name": "string" // "Admin" || "Sales Staff" || "Warehouse Staff"
      }
    }
  }
}
```

---

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "username": "Username is required.",
    "password": "Password is required."
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
  "message": "Get user succesfully.",
  "data": {
    "username": "string",
    "employeeId": "string",
    "fullName": "string",
    "role": {
      "id": "number",
      "name": "string"
    }
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
  "username": "string",
  "password": "string",
  "employeeId": "string",
  "roleId": "number"
}
```

**Response 201**

```json
{
  "success": true,
  "message": "Account created.",
  "data": {
    "username": "string",
    "employeeId": "string",
    "roleId": "number"
  }
}
```

**Response 400**

```json
{
  "success": false,
  "message": "Validation failed.",
  "errors": {
    "username": "Username already exists.",
    "employeeId": "This employee already has an account."
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
