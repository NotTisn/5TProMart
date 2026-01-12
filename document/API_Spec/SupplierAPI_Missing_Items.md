# 📋 Supplier API - What You're Still Missing

**Status Check Date:** January 9, 2026  
**Current Implementation:** ~75% Complete

---

## ❌ CRITICAL MISSING ITEMS

### 🔥 **1. MISSING QUERY/SEARCH ENDPOINT** (BLOCKING!)

**What's Missing:**
```java
GET /api/suppliers/?search={string}&supplierType={string}
```

**Your Current Controller:**
```java
// ❌ NO query endpoint - you only have:
GET /api/suppliers/{supplierId}  // Get by ID
POST /api/suppliers              // Create
PUT /api/suppliers/{supplierId}  // Update
DELETE /api/suppliers/{supplierId} // Delete
```

**What Frontend Needs:**
- List all suppliers with pagination
- Search by supplier name OR supplier ID
- Filter by supplier type
- Sort results

**Action Required:** Add this endpoint to `SupplierController.java`

---

### 🔥 **2. VALIDATION ISSUES IN SupplierRequest**

**Current Issues:**

❌ **representName is marked @NotBlank** - but spec says it's **OPTIONAL!**
```java
// Your code:
@NotBlank(message = "Representative name is required")  // ❌ WRONG!
private String representName;

// Should be:
// NO validation annotation (optional field)
private String representName;
```

❌ **representPhoneNumber is marked @NotBlank** - but spec says it's **OPTIONAL!**
```java
// Your code:
@NotBlank(message = "Representative phone number is required")  // ❌ WRONG!
private String representPhoneNumber;

// Should be:
// NO validation annotation (optional field)
private String representPhoneNumber;
```

❌ **currentDebt should NOT be in request** - it's system-managed!
```java
// Your code:
@NotNull(message = "Current debt is required")  // ❌ WRONG!
private BigDecimal currentDebt;

// Should be:
// REMOVE THIS FIELD! currentDebt is not sent by frontend
```

**From Spec:**
```json
// POST /api/suppliers - Request Body
{
  "supplierName": "string",      // required ✅
  "phoneNumber": "string",       // required ✅
  "address": "string",           // required ✅
  "representName": "string",     // NOT REQUIRED ⚠️
  "representPhoneNumber": "string", // NOT REQUIRED ⚠️
  "supplierType": "string",      // required ✅
  "suppliedProductType": "string" // required ✅
}
// Note: currentDebt is NOT in request! ❌
```

---

### 🔥 **3. MISSING DELETE BUSINESS VALIDATION**

**Current Issue:**
Your `deleteSupplierById()` doesn't check business rules!

**Spec Requirements:**
- ❌ Check if supplier has import history → Return 400 error
- ❌ Check if supplier has outstanding debt → Return 409 error

**Current Code:**
```java
@DeleteMapping("/{supplierId}")
public ApiResponse deleteSupplierById(@PathVariable String supplierId) {
    supplierUseCase.deleteSupplierById(supplierId);  // ❌ Just deletes!
    return ApiResponse.builder().success(true).build();
}
```

**What Should Happen:**
```java
// In SupplierUseCase.deleteSupplierById():
public void deleteSupplierById(String supplierId) {
    Supplier supplier = getSupplierById(supplierId);
    
    // Check 1: Has import history?
    if (hasImportHistory(supplier)) {
        throw new SupplierHasImportHistoryException(supplierId);
    }
    
    // Check 2: Has outstanding debt?
    if (supplier.getCurrentDebt().compareTo(BigDecimal.ZERO) > 0) {
        throw new SupplierHasOutstandingDebtException(supplierId, supplier.getCurrentDebt());
    }
    
    supplierRepository.delete(supplier);
}
```

---

## ⚠️ MINOR ISSUES

### 4. Wrong Success Messages

**GET by ID:**
```java
// Your code:
.message("Successfully update supplier")  // ❌ Says "update" for GET!

// Should be:
.message("Get supplier detail successfully.")
```

**POST:**
```java
// Your code:
.message("Successfully added new supplier")

// Should be:
.message("Supplier created successfully.")
```

**PUT:**
```java
// Your code:
.message("Successfully update supplier")

// Should be:
.message("Supplier updated successfully.")  // Proper grammar
```

**DELETE:**
```java
// Your code:
.message("Successfully deleted supplier")

// Should be:
.message("Supplier deleted successfully.")
```

---

### 5. Phone Number Validation Pattern

**Your Pattern:**
```java
@Pattern(regexp = "^\\+?[0-9]{10,15}$")
```

**Spec Says:**
```
"Phone number must have 10 digits."
```

**Recommendation:**
```java
@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
```

Unless you want to support international format, stick to spec (10 digits only).

---

## 📝 QUICK FIX CHECKLIST

### Priority 1 - CRITICAL (Must Fix):

- [ ] **Remove `@NotBlank` from `representName`** (it's optional!)
- [ ] **Remove `@NotBlank` from `representPhoneNumber`** (it's optional!)
- [ ] **Remove `currentDebt` field from `SupplierRequest`** (not in request body!)
- [ ] **Implement query/search endpoint** `GET /api/suppliers/`
- [ ] **Add delete business validation** (check debt and import history)

### Priority 2 - Important (Should Fix):

- [ ] **Fix success messages** to match spec exactly
- [ ] **Fix phone number pattern** to exactly 10 digits (if following spec strictly)

### Priority 3 - Optional:

- [ ] Create database migration for new columns (`represent_name`, `represent_phone_number`)
- [ ] Add unit tests for optional fields handling
- [ ] Add integration tests for all endpoints

---

## 🎯 CORRECTED SupplierRequest.java

```java
package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required.")
    @Size(max = 255, message = "Supplier name must be less than 255 characters")
    private String supplierName;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String phoneNumber;

    // ✅ OPTIONAL - No validation
    private String representName;

    // ✅ OPTIONAL - Only validate format IF provided
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must have 10 digits.")
    private String representPhoneNumber;

    @NotBlank(message = "Supplier type is required.")
    @Pattern(regexp = "^(Doanh nghiệp|Tư nhân)$", message = "Supplier type must be 'Doanh nghiệp' or 'Tư nhân'.")
    private String supplierType;

    @NotBlank(message = "Supplied product type is required.")
    private String suppliedProductType;

    // ❌ REMOVE currentDebt - it's not in request body!
    // currentDebt is system-managed, defaults to 0 on creation
}
```

---

## 🚀 NEXT STEPS

### Step 1: Fix SupplierRequest (5 minutes)
1. Remove `@NotBlank` from `representName`
2. Remove `@NotBlank` from `representPhoneNumber`  
3. Remove `currentDebt` field entirely
4. Fix phone number regex to `^[0-9]{10}$`

### Step 2: Implement Query Endpoint (30 minutes)
1. Create `SupplierSearchQuery.java`
2. Add `GET /api/suppliers/` to controller
3. Implement search logic in use case
4. Use Specification pattern for dynamic queries

### Step 3: Add Delete Validation (15 minutes)
1. Create domain exceptions
2. Add business checks in use case
3. Add exception handlers in GlobalExceptionHandler

### Step 4: Fix Messages (2 minutes)
Update all success messages to match spec

---

## 📊 Completion Estimate

| Task | Time | Priority |
|------|------|----------|
| Fix SupplierRequest validation | 5 min | 🔥 Critical |
| Implement query endpoint | 30 min | 🔥 Critical |
| Add delete validation | 15 min | 🔥 Critical |
| Fix success messages | 2 min | ⚠️ Important |
| Database migration | 5 min | ⚠️ Important |

**Total Time: ~1 hour**  
**After Fixes: 95% API Compliance** ✅

---

**Would you like me to implement these fixes for you?** 🚀
