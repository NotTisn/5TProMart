# 🏗️ Clean Architecture Review - 5TProMart

**Date:** January 7, 2026  
**Project:** 5TProMart E-commerce System  
**Architecture:** Clean Architecture (Hexagonal/Ports & Adapters)

---

## 📊 Overall Score: **6.5/10** ⭐

### Score Breakdown

| Category | Current Score | Target Score | Priority |
|----------|--------------|--------------|----------|
| **Structure & Organization** | 9/10 | 10/10 | Low |
| **Domain Independence** | 3/10 | 10/10 | 🔥 CRITICAL |
| **Dependency Direction** | 5/10 | 9/10 | High |
| **Port & Adapter Pattern** | 8/10 | 9/10 | Medium |
| **Use Case Design** | 6/10 | 9/10 | High |
| **Presentation Layer** | 8/10 | 9/10 | Low |

---

## ❌ Critical Issues Found

### 🔴 1. Domain Layer Depends on Infrastructure (CRITICAL!)

**Problem:** Domain models import infrastructure exceptions, violating the Dependency Rule.

**Files Affected:**
- `domain/model/Customer.java`
- `domain/model/Product.java`
- `domain/model/Category.java`
- `domain/model/Supplier.java`
- `domain/model/PendingRegistration.java`

**Current Code (Wrong):**
```java
package com.fivetpromart.domain.model;

import com.fivetpromart.infrastructure.error.AppException;  // ❌ Domain → Infrastructure
import com.fivetpromart.infrastructure.error.ErrorCode;     // ❌ VIOLATION!

public class Customer {
    public static Customer create(String fullName, String phoneNumber, ...) {
        if (fullName == null || fullName.isBlank()) {
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY); // ❌ Using infrastructure code
        }
    }
}
```

**Impact:**
- Domain cannot exist independently
- Cannot test domain logic without infrastructure
- Cannot change exception mechanism without touching domain
- **This is the #1 violation of Clean Architecture!**

---

### 🔴 2. Use Cases Return Domain Entities Instead of DTOs

**Problem:** Use cases expose domain entities directly to presentation layer.

**File:** `application/usecase/CustomerUseCase.java`

**Current Code (Wrong):**
```java
@Override
public Customer getCustomerById(String customerId) {  // ❌ Returns domain entity!
    return customerRepository.findById(customerId)
            .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));
}
```

**Impact:**
- Presentation layer sees entire domain entity
- No control over what data is exposed
- Breaks encapsulation
- Domain changes ripple to presentation

---

### ⚠️ 3. Missing Use Case Port Interfaces

**Problem:** Not all use cases implement port interfaces.

**Files Affected:**
- `AuthenticationUseCase.java` - No interface
- `SupplierUseCase.java` - Interface not complete
- `StockInventoryUseCase.java` - Interface not complete

**Current Code (Wrong):**
```java
@Service
@RequiredArgsConstructor
public class AuthenticationUseCase { // ❌ No interface!
    private final IdentityProviderPort identityProviderPort;
    // ...
}
```

**Impact:**
- Harder to test (cannot easily mock)
- Violates Dependency Inversion Principle
- Controllers depend on concrete classes

---

### ⚠️ 4. No Value Objects for Domain Primitives

**Problem:** Using raw `String`, `double` for important domain concepts.

**Examples:**
- Phone numbers as `String` (no validation)
- Email as `String` (no format validation)
- Money/prices as `double` (precision issues)

**Impact:**
- Validation logic scattered
- No type safety
- Easy to pass wrong data (e.g., email as phone number)
- Duplicate validation code

---

### ⚠️ 5. Empty Use Case Implementations

**Problem:** Some methods return `null` or are not implemented.

**File:** `application/usecase/StockInventoryUseCase.java`

**Current Code:**
```java
@Override
public StockInventoryDto createStockInventory(StockInventoryCreationCommand dto) {
    return null; // ❌ Not implemented!
}
```

**Impact:**
- Runtime errors when called
- Unclear which features are available
- Poor user experience

---

## ✅ What You're Doing Right

### 1. **Excellent Project Structure**

```
✓ Clear 4-layer separation (domain, application, infrastructure, presentation)
✓ Proper package organization
✓ Port interfaces in application layer
✓ Adapters in infrastructure layer
```

### 2. **Strong Domain Modeling**

```
✓ Factory methods (create(), reconstitute())
✓ Behavior methods (addLoyaltyPoints(), redeemLoyaltyPoints())
✓ Protected constructors (prevents invalid creation)
✓ Business logic in domain entities
```

### 3. **Proper Repository Pattern**

```
✓ Interface defined in application/port/out/
✓ Implementation in infrastructure/persistence/
✓ Use cases depend on interface, not implementation
```

### 4. **Clean Presentation Layer**

```
✓ Controllers are thin (no business logic)
✓ Proper request/response DTOs
✓ Presentation mappers separate data transformation
```

### 5. **Mapper Separation**

```
✓ Application mappers (Domain ↔ DTO)
✓ Persistence mappers (Domain ↔ DBO)
✓ Presentation mappers (DTO ↔ Request/Response)
```

---

## 🔧 Improvement Roadmap

### 🔥 PRIORITY 1: Fix Critical Domain Violations (1-2 days)

**Goal:** Make domain layer independent of infrastructure.

#### Step 1: Create Domain Exceptions

Create these new files:

1. **`domain/exception/DomainException.java`**
```java
package com.fivetpromart.domain.exception;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

2. **`domain/exception/InvalidCustomerDataException.java`**
```java
package com.fivetpromart.domain.exception;

public class InvalidCustomerDataException extends DomainException {
    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
```

3. **`domain/exception/InvalidProductDataException.java`**
```java
package com.fivetpromart.domain.exception;

public class InvalidProductDataException extends DomainException {
    public InvalidProductDataException(String message) {
        super(message);
    }
}
```

4. **`domain/exception/InvalidCategoryDataException.java`**
```java
package com.fivetpromart.domain.exception;

public class InvalidCategoryDataException extends DomainException {
    public InvalidCategoryDataException(String message) {
        super(message);
    }
}
```

5. **`domain/exception/InvalidSupplierDataException.java`**
```java
package com.fivetpromart.domain.exception;

public class InvalidSupplierDataException extends DomainException {
    public InvalidSupplierDataException(String message) {
        super(message);
    }
}
```

6. **`domain/exception/ResourceNotFoundException.java`**
```java
package com.fivetpromart.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' not found", resourceType, resourceId));
    }
}
```

#### Step 2: Update Domain Models

**For each domain model, replace:**

❌ Remove:
```java
import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;

throw new AppException(ErrorCode.CANNOT_BE_EMPTY);
```

✅ Add:
```java
import com.fivetpromart.domain.exception.InvalidCustomerDataException;

throw new InvalidCustomerDataException("Customer name cannot be empty");
```

**Files to update:**
- [ ] `domain/model/Customer.java`
- [ ] `domain/model/Product.java`
- [ ] `domain/model/Category.java`
- [ ] `domain/model/Supplier.java`
- [ ] `domain/model/PendingRegistration.java`

#### Step 3: Update GlobalExceptionHandler

Add domain exception handlers in `infrastructure/error/GlobalExceptionHandler.java`:

```java
@ExceptionHandler(InvalidCustomerDataException.class)
public ResponseEntity<ApiResponse> handleInvalidCustomerData(InvalidCustomerDataException ex) {
    log.warn("Invalid customer data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest().body(response);
}

@ExceptionHandler(InvalidProductDataException.class)
public ResponseEntity<ApiResponse> handleInvalidProductData(InvalidProductDataException ex) {
    log.warn("Invalid product data: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest().body(response);
}

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.warn("Resource not found: {}", ex.getMessage());
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}

@ExceptionHandler(DomainException.class)
public ResponseEntity<ApiResponse> handleDomainException(DomainException ex) {
    log.error("Domain exception: {}", ex.getMessage(), ex);
    
    ApiResponse response = ApiResponse.builder()
            .success(false)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest().body(response);
}
```

#### Step 4: Verify

Run tests and verify:
- [ ] Domain layer has NO imports from infrastructure
- [ ] Domain layer has NO imports from application
- [ ] Domain layer has NO imports from presentation
- [ ] Domain layer only imports: `java.*`, `lombok`, and other domain packages
- [ ] All tests pass

**Expected Score After Priority 1:** 8.0/10 ⭐⭐⭐⭐

---

### ⚠️ PRIORITY 2: Fix Use Case Design (2-3 days)

#### Step 1: Create Complete Use Case Port Interfaces

1. **`application/port/in/ICustomerUseCasePort.java`**
```java
package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;

import java.util.List;

public interface ICustomerUseCasePort {
    CustomerDto createCustomer(CustomerCreationCommand command);
    CustomerDto getCustomerById(String customerId);
    List<CustomerDto> getAllCustomers();
    CustomerDto updateCustomer(String customerId, CustomerUpdateCommand command);
    void deleteCustomer(String customerId);
}
```

2. **`application/port/in/IProductUseCasePort.java`**
```java
package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;

import java.util.List;

public interface IProductUseCasePort {
    ProductDto createProduct(ProductCreationCommand command);
    ProductDto getProductById(String productId);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(String productId, ProductUpdateCommand command);
    void deleteProduct(String productId);
}
```

3. **`application/port/in/IAuthenticationUseCasePort.java`**
```java
package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.AuthenticationTokensDto;
import com.fivetpromart.application.dto.ProfileDto;

public interface IAuthenticationUseCasePort {
    AuthenticationTokensDto authenticate(String username, String password);
    AuthenticationTokensDto refreshToken(String refreshToken);
    void logout(String userId);
    ProfileDto getCurrentUser(String accessToken);
}
```

4. Create similar interfaces for:
   - [ ] `ISupplierUseCasePort`
   - [ ] `IStockInventoryUseCasePort`
   - [ ] `ICategoryUseCasePort`

#### Step 2: Update Use Cases to Return DTOs

**Pattern to follow:**

❌ Before:
```java
@Override
public Customer getCustomerById(String customerId) {
    return customerRepository.findById(customerId).orElseThrow();
}
```

✅ After:
```java
@Override
public CustomerDto getCustomerById(String customerId) {
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
    return mapper.toDto(customer);
}
```

**Files to update:**
- [ ] `application/usecase/CustomerUseCase.java`
- [ ] `application/usecase/ProductUseCase.java`
- [ ] `application/usecase/SupplierUseCase.java`
- [ ] `application/usecase/CategoryUseCase.java`
- [ ] `application/usecase/AuthenticationUseCase.java`

#### Step 3: Implement or Mark Empty Methods

For methods that return `null`:

Option A - Implement them:
```java
@Override
public StockInventoryDto createStockInventory(StockInventoryCreationCommand command) {
    // Implement business logic
    StockInventory inventory = StockInventory.create(command.productId(), command.quantity());
    StockInventory saved = repository.save(inventory);
    return mapper.toDto(saved);
}
```

Option B - Mark as unsupported:
```java
@Override
public StockInventoryDto createStockInventory(StockInventoryCreationCommand command) {
    throw new UnsupportedOperationException("Stock inventory creation not yet implemented");
}
```

#### Step 4: Update Controllers to Use Port Interfaces

❌ Before:
```java
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerUseCase useCase; // Concrete class
}
```

✅ After:
```java
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final ICustomerUseCasePort useCase; // Interface
}
```

**Files to update:**
- [ ] `presentation/controller/CustomerController.java`
- [ ] `presentation/controller/ProductController.java`
- [ ] `presentation/controller/SupplierController.java`
- [ ] `presentation/controller/CategoryController.java`
- [ ] `presentation/controller/AuthenticationController.java`

**Expected Score After Priority 2:** 9.0/10 ⭐⭐⭐⭐⭐

---

### 💡 PRIORITY 3: Add Value Objects (3-5 days)

#### Step 1: Create Value Objects

1. **`domain/vo/PhoneNumber.java`**
```java
package com.fivetpromart.domain.vo;

import com.fivetpromart.domain.exception.InvalidCustomerDataException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PhoneNumber {
    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCustomerDataException("Phone number cannot be empty");
        }
        
        String cleaned = value.replaceAll("[^0-9]", "");
        if (cleaned.length() != 10) {
            throw new InvalidCustomerDataException("Phone number must be 10 digits");
        }
        
        return new PhoneNumber(cleaned);
    }

    @Override
    public String toString() {
        return value;
    }
}
```

2. **`domain/vo/Email.java`**
```java
package com.fivetpromart.domain.vo;

import com.fivetpromart.domain.exception.InvalidCustomerDataException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class Email {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidCustomerDataException("Email cannot be empty");
        }
        
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new InvalidCustomerDataException("Invalid email format");
        }
        
        return new Email(value.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
```

3. **`domain/vo/Money.java`**
```java
package com.fivetpromart.domain.vo;

import com.fivetpromart.domain.exception.InvalidProductDataException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@EqualsAndHashCode
public class Money {
    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductDataException("Amount must be positive");
        }
        
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), currency);
    }

    public static Money vnd(double amount) {
        return of(BigDecimal.valueOf(amount), "VND");
    }
    
    public static Money vnd(BigDecimal amount) {
        return of(amount, "VND");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new InvalidProductDataException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new InvalidProductDataException("Cannot subtract different currencies");
        }
        if (this.amount.compareTo(other.amount) < 0) {
            throw new InvalidProductDataException("Result would be negative");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw new InvalidProductDataException("Cannot multiply by negative quantity");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new InvalidProductDataException("Cannot compare different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
```

#### Step 2: Update Domain Models to Use Value Objects

**Example for Customer:**

❌ Before:
```java
public class Customer {
    private String phoneNumber;
    
    public static Customer create(String fullName, String phoneNumber, ...) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new InvalidCustomerDataException("Phone number cannot be empty");
        }
        if (phoneNumber.length() != 10) {
            throw new InvalidCustomerDataException("Phone number must be 10 digits");
        }
        
        Customer customer = new Customer();
        customer.phoneNumber = phoneNumber;
        // ...
    }
}
```

✅ After:
```java
import com.fivetpromart.domain.vo.PhoneNumber;

public class Customer {
    private PhoneNumber phoneNumber;
    
    public static Customer create(String fullName, String phoneNumber, ...) {
        Customer customer = new Customer();
        customer.phoneNumber = PhoneNumber.of(phoneNumber); // Validation in value object!
        // ...
    }
    
    public String getPhoneNumber() {
        return phoneNumber.getValue();
    }
}
```

**Example for Product:**

❌ Before:
```java
public class Product {
    private double price;
}
```

✅ After:
```java
import com.fivetpromart.domain.vo.Money;

public class Product {
    private Money price;
    
    public static Product create(String name, double price, ...) {
        Product product = new Product();
        product.price = Money.vnd(price);
        // ...
    }
    
    public Money getPrice() {
        return price;
    }
}
```

#### Step 3: Update Mappers to Handle Value Objects

**Application Mapper Example:**
```java
@Component
public class CustomerDataMapper {
    
    public CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getFullName(),
                customer.getPhoneNumber(), // Returns String from PhoneNumber.getValue()
                customer.getGender(),
                customer.getDateOfBirth(),
                customer.getRegistrationDate(),
                customer.getLoyaltyPoints()
        );
    }
}
```

**Persistence Mapper Example:**
```java
@Component
public class CustomerPersistenceMapper {
    
    public CustomerDbo toDbo(Customer customer) {
        CustomerDbo dbo = new CustomerDbo();
        dbo.setCustomerId(customer.getCustomerId());
        dbo.setPhoneNumber(customer.getPhoneNumber()); // String
        // ...
        return dbo;
    }
    
    public Customer toDomain(CustomerDbo dbo) {
        return Customer.reconstitute(
                dbo.getCustomerId(),
                dbo.getFullName(),
                dbo.getPhoneNumber(), // Will be converted to PhoneNumber in reconstitute
                dbo.getGender(),
                dbo.getDateOfBirth(),
                dbo.getRegistrationDate(),
                dbo.getLoyaltyPoints()
        );
    }
}
```

**Expected Score After Priority 3:** 9.5/10 ⭐⭐⭐⭐⭐

---

### 🎯 PRIORITY 4: Advanced Features (Optional)

#### 1. Add Domain Events

**Benefits:**
- Decouple business logic
- Enable event-driven architecture
- Audit trail for all business actions
- Easy to add new features without modifying existing code

**Create Base Event:**
```java
package com.fivetpromart.domain.event;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class DomainEvent {
    private final String eventId;
    private final Instant occurredAt;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
    }
}
```

**Create Specific Events:**
```java
package com.fivetpromart.domain.event;

import lombok.Getter;

@Getter
public class CustomerCreatedEvent extends DomainEvent {
    private final String customerId;
    private final String fullName;
    private final String phoneNumber;

    public CustomerCreatedEvent(String customerId, String fullName, String phoneNumber) {
        super();
        this.customerId = customerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
```

**Add to Domain Model:**
```java
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    public static Customer create(String fullName, String phoneNumber, ...) {
        Customer customer = new Customer();
        // ... set fields ...
        
        customer.registerEvent(new CustomerCreatedEvent(
                customer.customerId,
                customer.fullName,
                customer.phoneNumber.getValue()
        ));
        
        return customer;
    }
    
    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }
    
    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }
    
    public void clearEvents() {
        domainEvents.clear();
    }
}
```

**Publish in Adapter:**
```java
import org.springframework.context.ApplicationEventPublisher;

@Repository
@RequiredArgsConstructor
public class CustomerAdapter implements ICustomerRepository {
    
    private final ApplicationEventPublisher eventPublisher;
    private final CustomerJpaRepository jpaRepository;
    private final CustomerPersistenceMapper mapper;
    
    @Override
    public Customer save(Customer customer) {
        CustomerDbo dbo = mapper.toDbo(customer);
        CustomerDbo saved = jpaRepository.save(dbo);
        
        // Publish domain events
        customer.getDomainEvents().forEach(eventPublisher::publishEvent);
        customer.clearEvents();
        
        return mapper.toDomain(saved);
    }
}
```

#### 2. Add Aggregate Root Base Class

```java
package com.fivetpromart.domain.model;

import com.fivetpromart.domain.event.DomainEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<ID> {
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearEvents() {
        domainEvents.clear();
    }

    public abstract ID getId();
}
```

**Usage:**
```java
public class Customer extends AggregateRoot<String> {
    private String customerId;
    // ...
    
    @Override
    public String getId() {
        return customerId;
    }
}
```

#### 3. Add Architecture Tests (ArchUnit)

**Add dependency:**
```xml
<dependency>
    <groupId>com.tngtech.archunit</groupId>
    <artifactId>archunit-junit5</artifactId>
    <version>1.2.1</version>
    <scope>test</scope>
</dependency>
```

**Create test:**
```java
package com.fivetpromart.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class CleanArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.fivetpromart");

    @Test
    void domainShouldNotDependOnInfrastructure() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
                .check(classes);
    }

    @Test
    void domainShouldNotDependOnApplication() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..application..")
                .check(classes);
    }

    @Test
    void domainShouldNotDependOnPresentation() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..presentation..")
                .check(classes);
    }

    @Test
    void applicationShouldNotDependOnInfrastructure() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
                .check(classes);
    }

    @Test
    void applicationShouldNotDependOnPresentation() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..presentation..")
                .check(classes);
    }
}
```

---

## 📈 Progress Tracking

### Checklist - Priority 1 (Critical)

- [ ] Create `domain/exception/DomainException.java`
- [ ] Create `domain/exception/InvalidCustomerDataException.java`
- [ ] Create `domain/exception/InvalidProductDataException.java`
- [ ] Create `domain/exception/InvalidCategoryDataException.java`
- [ ] Create `domain/exception/InvalidSupplierDataException.java`
- [ ] Create `domain/exception/ResourceNotFoundException.java`
- [ ] Update `domain/model/Customer.java` (remove infrastructure imports)
- [ ] Update `domain/model/Product.java` (remove infrastructure imports)
- [ ] Update `domain/model/Category.java` (remove infrastructure imports)
- [ ] Update `domain/model/Supplier.java` (remove infrastructure imports)
- [ ] Update `domain/model/PendingRegistration.java` (remove infrastructure imports)
- [ ] Update `infrastructure/error/GlobalExceptionHandler.java` (add domain exception handlers)
- [ ] Run tests to verify all pass

### Checklist - Priority 2 (High)

- [ ] Create `application/port/in/ICustomerUseCasePort.java`
- [ ] Create `application/port/in/IProductUseCasePort.java`
- [ ] Create `application/port/in/ISupplierUseCasePort.java`
- [ ] Create `application/port/in/ICategoryUseCasePort.java`
- [ ] Create `application/port/in/IStockInventoryUseCasePort.java`
- [ ] Create `application/port/in/IAuthenticationUseCasePort.java`
- [ ] Update `CustomerUseCase.java` to return DTOs
- [ ] Update `ProductUseCase.java` to return DTOs
- [ ] Update `SupplierUseCase.java` to return DTOs
- [ ] Update `CategoryUseCase.java` to return DTOs
- [ ] Update `StockInventoryUseCase.java` (implement or mark as unsupported)
- [ ] Update `AuthenticationUseCase.java` to implement port
- [ ] Update all controllers to use port interfaces
- [ ] Run tests to verify all pass

### Checklist - Priority 3 (Medium)

- [ ] Create `domain/vo/PhoneNumber.java`
- [ ] Create `domain/vo/Email.java`
- [ ] Create `domain/vo/Money.java`
- [ ] Update `Customer.java` to use `PhoneNumber`
- [ ] Update `Product.java` to use `Money`
- [ ] Update application mappers to handle value objects
- [ ] Update persistence mappers to handle value objects
- [ ] Update presentation mappers to handle value objects
- [ ] Run tests to verify all pass

### Checklist - Priority 4 (Optional)

- [ ] Create domain event base class
- [ ] Create specific domain events (CustomerCreated, ProductCreated, etc.)
- [ ] Update domain models to register events
- [ ] Update adapters to publish events
- [ ] Create aggregate root base class
- [ ] Update domain models to extend aggregate root
- [ ] Add ArchUnit dependency
- [ ] Create architecture tests
- [ ] Document architecture decisions (ADRs)

---

## 🎯 Expected Outcomes

### After Priority 1
- **Score:** 8.0/10
- **Benefits:**
  - Domain is fully independent
  - Can test business logic without infrastructure
  - Easy to change exception handling
  - True Clean Architecture achieved

### After Priority 2
- **Score:** 9.0/10
- **Benefits:**
  - Use cases properly encapsulated
  - Controllers depend on abstractions
  - Easy to test with mocks
  - Better separation of concerns

### After Priority 3
- **Score:** 9.5/10
- **Benefits:**
  - Type-safe domain primitives
  - Centralized validation logic
  - No duplicate validation code
  - Self-documenting code

### After Priority 4
- **Score:** 10/10
- **Benefits:**
  - Event-driven architecture support
  - Audit trail for business actions
  - Automated architecture validation
  - Production-ready Clean Architecture

---

## 📚 Resources & References

### Clean Architecture
- **Book:** "Clean Architecture" by Robert C. Martin
- **Blog:** https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html
- **Video:** https://www.youtube.com/watch?v=Nsjsiz2A9mg

### Domain-Driven Design (DDD)
- **Book:** "Domain-Driven Design" by Eric Evans
- **Book:** "Implementing Domain-Driven Design" by Vaughn Vernon
- **Website:** https://www.domainlanguage.com/

### Hexagonal Architecture
- **Article:** https://alistair.cockburn.us/hexagonal-architecture/
- **Blog:** https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/

### Value Objects
- **Article:** https://martinfowler.com/bliki/ValueObject.html
- **Blog:** https://enterprisecraftsmanship.com/posts/value-objects-explained/

### Testing
- **Book:** "Unit Testing Principles, Practices, and Patterns" by Vladimir Khorikov
- **Library:** ArchUnit - https://www.archunit.org/

---

## 💬 FAQs

### Q: Tại sao phải tách exception thành domain exception?
**A:** Vì domain layer phải **độc lập hoàn toàn**. Nếu domain phụ thuộc vào infrastructure exception, bạn không thể:
- Test domain logic mà không cần infrastructure
- Tái sử dụng domain trong project khác
- Thay đổi exception mechanism mà không sửa domain
- Đây là vi phạm nghiêm trọng nhất trong Clean Architecture!

### Q: Có nhất thiết phải dùng value objects không?
**A:** Không bắt buộc, nhưng **rất nên dùng** vì:
- Validation logic tập trung một chỗ
- Type-safe (không bị nhầm lẫn phone với email)
- Code tự document (nhìn là biết PhoneNumber)
- Dễ bảo trì (sửa validation chỉ một chỗ)

### Q: Use case nên return DTO hay entity?
**A:** **Luôn luôn return DTO!** Lý do:
- Presentation layer không nên biết domain entity
- Kiểm soát được dữ liệu nào được expose
- Dễ thêm/bớt field mà không ảnh hưởng domain
- Tuân thủ nguyên tắc encapsulation

### Q: Khi nào nên dùng Clean Architecture?
**A:** Dùng khi:
- Project sống lâu (5+ năm)
- Business logic phức tạp
- Team lớn (3+ devs)
- Cần test coverage cao
- Có nhiều client (web, mobile, API)
- Không dùng cho CRUD đơn giản!

### Q: Database change có ảnh hưởng gì?
**A:** **Chỉ ảnh hưởng infrastructure layer!**
- Domain: KHÔNG thay đổi
- Application: KHÔNG thay đổi
- Presentation: KHÔNG thay đổi
- Infrastructure: Chỉ đổi DBO annotations và repository implementation

### Q: Làm thế nào để verify architecture đúng?
**A:** Dùng **ArchUnit tests** để tự động kiểm tra:
- Domain không import infrastructure
- Domain không import application
- Domain không import presentation
- Application không import infrastructure
- Application không import presentation

---

## 🎓 Summary

### Current State
- ✅ Structure: Excellent (9/10)
- ❌ Domain Independence: Critical Issue (3/10)
- ⚠️ Use Case Design: Needs Improvement (6/10)
- ✅ Presentation Layer: Good (8/10)

### Critical Issues
1. **Domain depends on infrastructure** (MUST FIX!)
2. Use cases return entities instead of DTOs
3. Missing use case port interfaces
4. No value objects for domain primitives

### Action Required
1. **Fix domain exceptions** (Priority 1) → Score: 8/10
2. **Fix use case design** (Priority 2) → Score: 9/10
3. **Add value objects** (Priority 3) → Score: 9.5/10
4. **Add advanced features** (Priority 4) → Score: 10/10

### Timeline
- **Priority 1:** 1-2 days
- **Priority 2:** 2-3 days
- **Priority 3:** 3-5 days
- **Priority 4:** Optional

### Final Goal
Transform from **6.5/10** to **9.5-10/10** Clean Architecture implementation in **1-2 weeks**.

---

**Last Updated:** January 7, 2026  
**Next Review:** After completing Priority 1 & 2

---

> **Note:** This is a living document. Update it as you progress through the improvements!
