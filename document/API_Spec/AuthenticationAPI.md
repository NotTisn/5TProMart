# Authentication API - Frontend Integration Guide

## Overview
This document describes the authentication flow for the 5TProMart application. The API uses **HttpOnly cookies** for secure refresh token storage and **JWT access tokens** for API authorization.

---

## 🔐 Security Architecture

### Token Management Strategy
- **Access Token**: Short-lived JWT token sent in response body, stored in memory/localStorage (use with caution)
- **Refresh Token**: Long-lived token stored in **HttpOnly cookie** (not accessible via JavaScript)
- **Cookie Name**: `refresh_token`
- **Cookie Properties**: 
  - HttpOnly: ✅ (prevents XSS attacks)
  - Secure: ✅ (HTTPS only in production)
  - SameSite: Lax/Strict (prevents CSRF attacks)

### Why HttpOnly Cookies?
- **XSS Protection**: JavaScript cannot access the refresh token
- **Automatic Handling**: Browser automatically sends cookie with requests
- **CSRF Protection**: Combined with SameSite attribute

---

## 📋 API Endpoints

### Base URL
```
http://localhost:8080/api/v1/auth
```

---

## 1. Login (Sign In)

### Endpoint
```http
POST /api/v1/auth/login
```

### Request Headers
```http
Content-Type: application/json
```

### Request Body
```json
{
  "email": "user@example.com",
  "password": "your_password"
}
```

#### Field Validation
| Field    | Type   | Required | Validation                    |
|----------|--------|----------|-------------------------------|
| email    | string | ✅       | Must not be blank             |
| password | string | ✅       | Must not be blank             |

### Success Response (200 OK)
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Successfully logged in",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": null,
    "idToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "scope": "openid profile email",
    "authenticated": true,
    "lastLogin": "2026-01-09T14:30:00"
  }
}
```

**Note**: `refreshToken` is `null` in response body because it's stored in HttpOnly cookie.

#### Response Cookies
```http
Set-Cookie: refresh_token={token_value}; Path=/; HttpOnly; Secure; SameSite=Lax; Max-Age=604800
```
- **Max-Age**: 604800 seconds (7 days)

### Error Responses

#### 400 Bad Request (Validation Error)
```json
{
  "success": false,
  "statusCode": 400,
  "message": "Username is required"
}
```

#### 401 Unauthorized (Invalid Credentials)
```json
{
  "success": false,
  "statusCode": 401,
  "message": "Invalid email or password"
}
```

### Frontend Implementation Example

#### JavaScript (Fetch API)
```javascript
async function login(email, password) {
  try {
    const response = await fetch('http://localhost:8080/api/v1/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include', // ⚠️ IMPORTANT: Include cookies
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (data.success) {
      // Store access token (use memory or localStorage with caution)
      localStorage.setItem('accessToken', data.data.accessToken);
      
      // Refresh token is automatically stored in HttpOnly cookie
      console.log('Login successful!');
      return data.data;
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Login failed:', error);
    throw error;
  }
}
```

#### Axios
```javascript
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  withCredentials: true, // ⚠️ IMPORTANT: Include cookies
});

async function login(email, password) {
  try {
    const response = await apiClient.post('/auth/login', {
      email,
      password
    });

    const { data } = response.data;
    
    // Store access token
    localStorage.setItem('accessToken', data.accessToken);
    
    return data;
  } catch (error) {
    console.error('Login failed:', error.response?.data?.message);
    throw error;
  }
}
```

---

## 2. Logout (Sign Out)

### Endpoint
```http
POST /api/v1/auth/logout
```

### Request Headers
```http
Cookie: refresh_token={token_value}
```

**Note**: No request body needed. Refresh token is read from HttpOnly cookie.

### Success Response (200 OK)
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Request successful",
  "data": "Logged out successfully"
}
```

#### Response Cookies
```http
Set-Cookie: refresh_token=; Path=/; HttpOnly; Secure; SameSite=Lax; Max-Age=0
```
- **Max-Age=0**: Immediately deletes the cookie

### Backend Behavior
1. Reads `refresh_token` from cookie
2. Revokes token in Keycloak (if present)
3. **Always** deletes the HttpOnly cookie (even if token is missing)

### Frontend Implementation Example

#### JavaScript (Fetch API)
```javascript
async function logout() {
  try {
    const response = await fetch('http://localhost:8080/api/v1/auth/logout', {
      method: 'POST',
      credentials: 'include', // ⚠️ IMPORTANT: Send cookies
    });

    const data = await response.json();

    if (data.success) {
      // Clear access token from storage
      localStorage.removeItem('accessToken');
      
      // Cookie is automatically deleted by server
      console.log('Logout successful!');
      
      // Redirect to login page
      window.location.href = '/login';
    }
  } catch (error) {
    console.error('Logout failed:', error);
    // Still clear local storage and redirect
    localStorage.removeItem('accessToken');
    window.location.href = '/login';
  }
}
```

#### Axios
```javascript
async function logout() {
  try {
    await apiClient.post('/auth/logout');
    
    // Clear access token
    localStorage.removeItem('accessToken');
    
    // Redirect
    window.location.href = '/login';
  } catch (error) {
    console.error('Logout failed:', error);
    localStorage.removeItem('accessToken');
    window.location.href = '/login';
  }
}
```

---

## 3. Refresh Token (Get New Access Token)

### Endpoint
```http
POST /api/v1/auth/refresh-token
```

### Request Headers
```http
Cookie: refresh_token={token_value}
```

**Note**: No request body needed. Refresh token is read from HttpOnly cookie.

### Success Response (200 OK)
```json
{
  "success": true,
  "statusCode": 200,
  "message": "Request successful",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### Response Cookies
```http
Set-Cookie: refresh_token={new_token_value}; Path=/; HttpOnly; Secure; SameSite=Lax; Max-Age=2592000
```
- **Max-Age**: 2592000 seconds (30 days)
- **Note**: Backend issues a **new refresh token** for security (token rotation)

### Error Responses

#### 401 Unauthorized (Missing Refresh Token)
```json
{
  "success": false,
  "statusCode": 401,
  "message": "Refresh token missing."
}
```

#### 401 Unauthorized (Expired/Invalid Token)
```json
{
  "success": false,
  "statusCode": 401,
  "message": "Session expired. Please log in again."
}
```

**Note**: When refresh token fails, the HttpOnly cookie is **automatically deleted** by the server.

### Frontend Implementation Example

#### JavaScript (Fetch API)
```javascript
async function refreshAccessToken() {
  try {
    const response = await fetch('http://localhost:8080/api/v1/auth/refresh-token', {
      method: 'POST',
      credentials: 'include', // ⚠️ IMPORTANT: Send cookies
    });

    if (response.status === 401) {
      // Session expired, redirect to login
      console.log('Session expired. Redirecting to login...');
      localStorage.removeItem('accessToken');
      window.location.href = '/login';
      return null;
    }

    const data = await response.json();

    if (data.success) {
      // Update access token
      localStorage.setItem('accessToken', data.data.accessToken);
      console.log('Access token refreshed!');
      return data.data.accessToken;
    }
  } catch (error) {
    console.error('Token refresh failed:', error);
    localStorage.removeItem('accessToken');
    window.location.href = '/login';
    return null;
  }
}
```

#### Axios with Interceptor (Automatic Token Refresh)
```javascript
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  withCredentials: true,
});

// Request interceptor: Add access token to all requests
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: Handle 401 errors and refresh token
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If 401 and not already retrying
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // Queue this request while token is refreshing
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return apiClient(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Attempt to refresh token
        const response = await apiClient.post('/auth/refresh-token');
        const { accessToken } = response.data.data;

        // Store new token
        localStorage.setItem('accessToken', accessToken);

        // Update default header
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

        // Process queued requests
        processQueue(null, accessToken);

        // Retry original request
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return apiClient(originalRequest);
      } catch (refreshError) {
        // Refresh failed, logout user
        processQueue(refreshError, null);
        localStorage.removeItem('accessToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
```

---

## 🔄 Complete Authentication Flow

### 1. Initial Login
```
User enters credentials
    ↓
POST /auth/login
    ↓
Server validates credentials
    ↓
Server issues:
  - Access Token (in response body)
  - Refresh Token (in HttpOnly cookie)
    ↓
Client stores:
  - Access Token (localStorage/memory)
  - Refresh Token (automatic, in cookie)
```

### 2. Making Authenticated Requests
```
Client makes API request
    ↓
Add: Authorization: Bearer {accessToken}
    ↓
If 401 Unauthorized:
    ↓
Attempt token refresh
    ↓
Retry original request with new token
```

### 3. Token Refresh Flow
```
Access token expires (short-lived)
    ↓
POST /auth/refresh-token
  (refresh_token sent automatically via cookie)
    ↓
Server validates refresh token
    ↓
Server issues:
  - New Access Token (in response body)
  - New Refresh Token (in HttpOnly cookie)
    ↓
Client updates Access Token in storage
```

### 4. Logout Flow
```
User clicks logout
    ↓
POST /auth/logout
  (refresh_token sent automatically via cookie)
    ↓
Server revokes token in Keycloak
    ↓
Server deletes refresh_token cookie
    ↓
Client clears Access Token from storage
    ↓
Redirect to login page
```

---

## ⚠️ Important Frontend Considerations

### 1. **Always Use `credentials: 'include'` or `withCredentials: true`**
```javascript
// Fetch API
fetch(url, { credentials: 'include' });

// Axios
axios.create({ withCredentials: true });
```
**Why?** Without this, browsers won't send/receive cookies in cross-origin requests.

### 2. **CORS Configuration**
Your backend must allow credentials in CORS:
```java
// Backend CORS config (already configured)
@CrossOrigin(
  origins = "http://localhost:3000", 
  allowCredentials = "true"
)
```

### 3. **Access Token Storage**
**Options:**
- **Memory (Recommended)**: Store in React state/Vuex/Redux. Lost on refresh but most secure.
- **localStorage (Caution)**: Vulnerable to XSS attacks. Sanitize all user inputs.
- **sessionStorage**: Cleared when tab closes. Better than localStorage.

**Never store refresh token in localStorage!** (It's already in HttpOnly cookie)

### 4. **Token Expiration Handling**
Implement automatic token refresh before expiration:
```javascript
// Decode JWT to check expiration
function isTokenExpiringSoon(token) {
  const payload = JSON.parse(atob(token.split('.')[1]));
  const expiresAt = payload.exp * 1000; // Convert to milliseconds
  const now = Date.now();
  const fiveMinutes = 5 * 60 * 1000;
  
  return expiresAt - now < fiveMinutes;
}

// Check periodically
setInterval(() => {
  const token = localStorage.getItem('accessToken');
  if (token && isTokenExpiringSoon(token)) {
    refreshAccessToken();
  }
}, 60000); // Check every minute
```

### 5. **Logout on Multiple Tabs**
Use `storage` event to synchronize logout across tabs:
```javascript
window.addEventListener('storage', (event) => {
  if (event.key === 'accessToken' && event.newValue === null) {
    // User logged out in another tab
    window.location.href = '/login';
  }
});
```

---

## 🧪 Testing with cURL

### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}' \
  -c cookies.txt \
  -v
```

### Refresh Token
```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh-token \
  -b cookies.txt \
  -c cookies.txt \
  -v
```

### Logout
```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -b cookies.txt \
  -v
```

---

## 🔒 Security Best Practices

### For Frontend Developers

1. **Always use HTTPS in production** (HttpOnly cookies require Secure flag)
2. **Never log/expose tokens** in console or error messages
3. **Implement CSRF protection** if not using SameSite cookies
4. **Sanitize user inputs** to prevent XSS attacks
5. **Clear tokens on logout** and redirect immediately
6. **Handle token expiration gracefully** with automatic refresh
7. **Don't store sensitive data in localStorage** if possible
8. **Implement proper error handling** for auth failures

### Token Lifetime Reference
- **Access Token**: ~15 minutes (configured in Keycloak)
- **Refresh Token (Login)**: 7 days
- **Refresh Token (Refresh)**: 30 days (rotated for security)

---

## 📝 Common Issues & Solutions

### Issue 1: Cookies Not Being Sent
**Solution**: Ensure `credentials: 'include'` or `withCredentials: true` is set.

### Issue 2: CORS Errors
**Solution**: Backend must explicitly allow credentials:
```java
allowCredentials = "true"
```

### Issue 3: Token Refresh Loop
**Solution**: Add `_retry` flag to prevent infinite retries:
```javascript
if (!originalRequest._retry) {
  originalRequest._retry = true;
  // ... retry logic
}
```

### Issue 4: Cookie Not Deleted on Logout
**Solution**: Server always deletes cookie. Check browser DevTools → Application → Cookies.

---

## 📞 Support

For backend issues or questions:
- Check `FIX_401_ERROR.md` for authentication debugging
- Review `CLEAN_ARCHITECTURE_REVIEW.md` for architecture details
- Contact backend team for Keycloak configuration

---

## 🎯 Quick Start Checklist

- [ ] Install Axios or configure Fetch API
- [ ] Set `withCredentials: true` or `credentials: 'include'`
- [ ] Implement login function
- [ ] Implement logout function
- [ ] Implement token refresh function
- [ ] Add Axios response interceptor for 401 handling
- [ ] Store access token in localStorage/memory
- [ ] Clear tokens on logout
- [ ] Test with backend running on `http://localhost:8080`
- [ ] Verify cookies in browser DevTools

---

**Last Updated**: January 9, 2026  
**API Version**: v1  
**Backend Port**: 8080  
**Authentication Provider**: Keycloak
