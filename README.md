# 5TProMart — Retail POS Management System

A full-stack point-of-sale system for Vietnamese convenience stores, built on strict Clean/Hexagonal Architecture with Spring Boot and React. Features a rich domain model with State, Strategy, and Factory patterns, Keycloak SSO, and complete retail operations (sales, inventory, purchasing, HR, analytics).

> **Frontend repo:** [FiveTProMart](https://github.com/ToanHuynh0201/FiveTProMart) (React + TypeScript + Chakra UI)

---

## Architecture

```
                        ┌─────────────────────────┐
                        │   React 19 Frontend      │
                        │  TypeScript · Chakra UI  │
                        │  Zustand · Recharts      │
                        └────────────┬────────────┘
                                     │ HTTPS
                                     ▼
                        ┌─────────────────────────┐
                        │   Spring Boot 3.5 API    │
                        │   18 Controllers         │
                        │   OAuth2 Resource Server │
                        └─────────────────────────┘
                                     │
                    ┌────────────────┼────────────────┐
                    ▼                ▼                ▼
           ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
           │  Keycloak    │ │ PostgreSQL   │ │   Brevo      │
           │  (SSO/OIDC)  │ │ (Data)       │ │  (Email/OTP) │
           └──────────────┘ └──────────────┘ └──────────────┘


  ╔══════════════════════════════════════════════════════════╗
  ║              HEXAGONAL ARCHITECTURE                      ║
  ║                                                          ║
  ║   ┌─────────────────────────────────────────────────┐   ║
  ║   │              PRESENTATION                        │   ║
  ║   │   18 REST Controllers · Request/Response DTOs    │   ║
  ║   └──────────────────────┬──────────────────────────┘   ║
  ║                          │ calls                         ║
  ║   ┌──────────────────────▼──────────────────────────┐   ║
  ║   │              APPLICATION                         │   ║
  ║   │   15 Input Ports (interfaces)                    │   ║
  ║   │   19 Use Cases (implementations)                 │   ║
  ║   │   22 Output Ports (interfaces)                   │   ║
  ║   │   DTOs · MapStruct Mappers                       │   ║
  ║   └──────────────────────┬──────────────────────────┘   ║
  ║                          │ calls output ports            ║
  ║   ┌──────────────────────▼──────────────────────────┐   ║
  ║   │              DOMAIN (innermost — no imports)     │   ║
  ║   │   25+ Entities · 57 Exceptions · Enums          │   ║
  ║   │   State Pattern · Strategy Pattern · Factories   │   ║
  ║   │   Zero framework dependencies                    │   ║
  ║   └─────────────────────────────────────────────────┘   ║
  ║                          ▲ implements output ports       ║
  ║   ┌──────────────────────┴──────────────────────────┐   ║
  ║   │              INFRASTRUCTURE                      │   ║
  ║   │   JPA Adapters · Keycloak Adapter · Brevo Email  │   ║
  ║   │   Schedulers · Config · Spring Security          │   ║
  ║   └─────────────────────────────────────────────────┘   ║
  ╚══════════════════════════════════════════════════════════╝

  Dependency rule: Domain → nothing. Application → Domain only.
  Infrastructure → Application + Domain. Presentation → Application.
  Zero violations (verified by import analysis).
```

---

## Domain Model

### Order Aggregate (State Pattern)

The `Order` entity (464 lines, no public setters) delegates state transitions to state objects:

```
                ┌─────────────────┐
                │  PendingOrder    │
                │     State       │
                │                 │
                │  pay() ─────────┼──→ PaidOrderState
                │  cancel() ──────┼──→ CancelledOrderState
                └─────────────────┘

                ┌─────────────────┐
                │  PaidOrder      │
                │     State       │
                │                 │
                │  cancel() ──────┼──→ CancelledOrderState (refund)
                │  pay() ─────────┼──→ throws IllegalOrderStateTransitionException
                └─────────────────┘

                ┌─────────────────┐
                │  CancelledOrder │
                │     State       │
                │                 │
                │  (all methods)──┼──→ throws (terminal state)
                └─────────────────┘
```

### Strategy Families (4 independent hierarchies)

| Strategy | Interface | Implementations | Example Logic |
|----------|-----------|-----------------|---------------|
| **Payment** | `PaymentStrategy` | `CashPaymentStrategy`, `BankTransferPaymentStrategy` | Cash: Vietnamese 1,000 VND rounding (HALF_UP). Bank: exact amount, generates transaction ref. |
| **Discount** | `DiscountStrategy` | `PercentageDiscount`, `FixedAmountDiscount`, `LoyaltyPointDiscount`, `NoDiscount` | Loyalty: configurable conversion rate (1 pt = 1 VND). Percentage: optional max cap. |
| **Promotion** | `PromotionStrategy` | `DiscountPromotionStrategy`, `BuyXGetYStrategy`, `NoPromotionStrategy` | Buy 2 Get 1: every 3 items → pay for 2 (full bundle math). |
| **Notification** | `NotificationStrategy` | `EmailNotificationStrategy`, `SmsNotificationStrategy`, `CompositeNotificationStrategy` | Composite: sends via multiple channels simultaneously. |

### Key Entities

```
Order ──→ OrderItem ──→ Product ──→ Category
  │                       │
  │                       ├── StockInventory (shelf + storage quantities)
  │                       └── StockLot (manufacture/expiry dates)
  │
  ├── Customer (loyalty points: earn 1% per purchase)
  │
  └── Staff ──→ Profile ──→ WorkSchedule ──→ WorkShift
                                              └── ShiftRoleConfig
                                              └── DailySalary
```

---

## Failure Handling

### Inventory Consistency

```
Order placed
    │
    ▼
StockReservation created (shelf quantity reserved)
    │
    ├── Payment succeeds → reservation consumed, stock decremented
    ├── Payment fails → reservation released
    └── Reservation expires → StockReservationExpiryJob releases it (background scheduler)
```

Stock is tracked in two locations (shelf vs. storage) with shelf-first deduction. The `StockInventoryExpiryJob` scheduler also flags lots approaching expiry dates.

### 57 Domain Exceptions (granular, not generic)

Instead of a generic `BadRequestException`, the domain throws specific exceptions that encode the business rule that was violated:

| Exception | Business Rule |
|-----------|---------------|
| `InsufficientLoyaltyPointsException` | Customer tried to redeem more points than available |
| `ExpiredLotException` | Attempt to sell from an expired stock lot |
| `StaffHasActiveOrdersException` | Cannot delete a staff member with open orders |
| `InsufficientStockException` | Available stock (total − reserved) < requested quantity |
| `MaxOtpAttemptsExceededException` | OTP brute-force protection (attempt limit hit) |
| `InvalidDebtPaymentException` | Supplier debt payment violates payment rules |

All 57 exceptions live in the domain layer — they carry no HTTP status codes or Spring annotations. The presentation layer maps them to HTTP responses.

---

## Design Decisions

**Why Hexagonal Architecture (not layered)?**
The payment strategy is the clearest example: adding a third payment method (e.g., MoMo e-wallet) requires one new class implementing `PaymentStrategy` in the domain and one new case in `PaymentStrategyFactory`. Zero changes to repositories, JPA adapters, controllers, or database schema. The hexagonal boundary means the most complex business logic (Vietnamese cash rounding, Buy-X-Get-Y bundle math, loyalty point conversion) is testable with plain JUnit — no Spring context, no database, no mocks of infrastructure.

**Why State Pattern for orders (not enum checks)?**
An `if (order.status == PENDING)` check in a service method is a bug waiting to happen — every new developer must remember every valid transition. The State pattern makes illegal transitions a compile-time or immediate-runtime guarantee: `CancelledOrderState.pay()` throws `IllegalOrderStateTransitionException`. The Order entity never exposes a `setStatus()` method.

**Why 57 domain exceptions (not 5 generic ones)?**
Each exception is a documented business rule. `InsufficientLoyaltyPointsException` tells the frontend exactly what happened — the client can show "You don't have enough points" without parsing an error message string. Generic exceptions force clients to pattern-match on error messages, which breaks when messages change.

**Why Keycloak (not custom JWT auth)?**
The system has 3+ roles with hierarchical permissions (Admin > Manager > SalesStaff, WarehouseStaff). Keycloak handles the identity lifecycle (registration, password reset, OTP, role assignment) so the application layer stays focused on business logic. The Keycloak adapter communicates via OpenFeign — the domain layer never imports a Keycloak class.

**Why separate shelf and storage stock?**
Vietnamese convenience stores physically separate front-shelf display stock from back-storage inventory. The deduction rule is shelf-first — if a customer buys 5 units and only 3 are on the shelf, 3 come from shelf and 2 from storage. This business rule lives in the `StockInventory` entity, not in a service class.

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java 21, Spring Boot 3.5, Spring Cloud 2025.0 |
| **Frontend** | React 19, TypeScript 5.9, Vite 7, Chakra UI, Zustand, Recharts |
| **Database** | PostgreSQL 16, Spring Data JPA, Hibernate |
| **Auth** | Keycloak 26 (OAuth2/OIDC), Spring Security, OTP via Brevo |
| **Mapping** | MapStruct 1.5.5 + Lombok |
| **Infrastructure** | Docker, Docker Compose (PostgreSQL + Keycloak + App) |

---

## Running Locally

```bash
# Start backing services
docker-compose up -d    # PostgreSQL 16, Keycloak 26 (auto-imports realm)

# Run backend
cd 5TProMart
mvn spring-boot:run

# Run frontend (separate terminal)
cd FiveTProMart
npm install && npm run dev
```

Backend: `http://localhost:8080` · Frontend: `http://localhost:5173` · Keycloak: `http://localhost:8080/auth`

---

## Stats

- **420 commits** across backend + frontend
- **748 source files** (527 Java, 221 TS/TSX)
- **25+ domain entities**, **57 domain exceptions**, **15 input ports**, **22 output ports**
- **4 Strategy pattern families**, **State pattern** for order lifecycle
- **~2 months** of development
