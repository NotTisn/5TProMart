# Database Seed System

Modular SQL seed data for local development and testing.

## Quick Start

```bash
# Option 1: Auto-seed on startup
dev.bat --seed

# Option 2: Seed existing tables (safe)
cd infrastructure\seed
seed-only.bat

# Option 3: Drop all + reseed (DESTRUCTIVE)
cd infrastructure\seed
rinse-and-seed.bat

# Check status
check-status.bat
```

## Structure

```
01_categories.sql          Product categories (10)
02_suppliers.sql           Suppliers (5)
03_products.sql            Products (30, 6k-150k VND)
04_stock_inventory.sql     Stock batches with expiry (30)
05_customers.sql           Customers with loyalty points (15)
06_promotions.sql          Active promotions (3)
master_seed.sql            Runs all modules in order
```

## Seed Data

- **10 Categories**: Electronics, Food, Beverages, Personal Care, etc.
- **30 Products**: 6,000 - 150,000 VND with realistic Vietnamese context
- **30 Stock Batches**: With manufacture/expiry dates, import prices
- **15 Customers**: Vietnamese names, loyalty points (340-3,200)
- **5 Suppliers**: ELECTRONICS, FOOD, DAIRY, BEVERAGES, PERSONAL_CARE types
- **3 Promotions**: Active Jan-Mar 2026 (percentage discount, buy-X-get-Y)

## Test Users (Keycloak)

Configured in `keycloak-config/fivetpro-realm.json`:

**⚠️ IMPORTANT: Login with EMAIL, not username**

| Email | Password | Username | Role |
|-------|----------|----------|------|
| `admin@fivetpromart.com` | `admin123` | admin | Admin (Full access) |
| `manager@fivetpromart.com` | `manager123` | manager | Manager (Read + limited write) |
| `sales@fivetpromart.com` | `sales123` | salesstaff | SalesStaff (Orders, customers) |
| `warehouse@fivetpromart.com` | `warehouse123` | warehousestaff | WarehouseStaff (Inventory) |

## Modular Design

EacIndividual Modules

Each SQL file is independent and uses `ON CONFLICT DO NOTHING` for idempotency.

```bash
# Run single module
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < 03_products.sql

# Reset specific entity
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "DELETE FROM products;"
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < 03_products.sql
```

### "Tables don't exist"
→ Start Spring Boot first (Hibernate will create tables), then run seeding

### "Duplicate key violation"
→ Data already exists. Use `rinse-and-seed.bat` to start fresh

### "PostgreSQL not running"
→ Run `docker ps` to check. Start with `dev.bat`

### "Seeding takes too long"
→ Comment out modules you don't need in `master_seed.sql`

## Design Principles

1. **Modular**: Each file is independent
2. **Idempotent**: Safe to run multiple times (`ON CONFLICT DO NOTHING`)
3. **Realistic**: Vietnamese context, proper prices, expiry dates
4. **Documented**: Clear comments, proper formatting
5. **Production-inspired**: Follows real-world data patterns

## Future Enhancements

- [ ] Add order history seed data
- [ ] Add staff profiles (linked to Keycloak users)
- [ ] Add purchase orders
- [ ] Add work shift data
- [ ] Performance metrics (seeding time tracking)

---

**Last Updated**: January 19, 2026  
**Maintainer**: 5TProMart Dev Team
| Issue | Solution |
|-------|----------|
| Tables don't exist | Start Spring Boot first, then seed |
| Duplicate key error | Use `rinse-and-seed.bat` for fresh start |
| PostgreSQL not running | Check `docker ps`, start with `dev.bat` |

## Notes

- All modules use `ON CONFLICT DO NOTHING` - safe to re-run
- Audit fields (`created_at`, `updated_at`) managed by Spring Data JPA
- Full seed takes ~2-3 seconds
- Modular design - run individual files or all via `master_seed.sql`