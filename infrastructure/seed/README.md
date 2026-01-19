# 5TProMart - Database Seed System

## Overview

This folder contains a **modular, production-ready** seed data system for local development and testing.

## Structure

```
seed/
├── master_seed.sql          ← Main entry point (runs all modules)
├── 01_categories.sql        ← Product categories
├── 02_suppliers.sql         ← Supplier data
├── 03_products.sql          ← 30 realistic products
├── 04_stock_inventory.sql   ← Stock batches with expiry dates
├── 05_customers.sql         ← 15 test customers with loyalty points
├── 06_promotions.sql        ← Active promotions
├── rinse-and-seed.bat       ← Drop schema + reseed (DESTRUCTIVE)
└── seed-only.bat            ← Seed existing tables (safe)
```

## Quick Start

### Option 1: Fresh Start (Drops Everything)
```bash
cd infrastructure/seed
rinse-and-seed.bat
```

### Option 2: Add Data to Existing Tables
```bash
cd infrastructure/seed
seed-only.bat
```

### Option 3: Automatic Seeding on Startup
```bash
cd 5TProMart_be
dev.bat --seed
```

## What Gets Seeded

| Entity | Count | Description |
|--------|-------|-------------|
| **Categories** | 10 | Electronics, Food, Beverages, Personal Care, etc. |
| **Suppliers** | 5 | Diverse supplier types (ELECTRONICS, FOOD, DAIRY, etc.) |
| **Products** | 30 | Realistic products with prices (12,000 VND - 150,000 VND) |
| **Stock Inventory** | 30 | Batches with manufacture/expiry dates, import prices |
| **Customers** | 15 | Vietnamese names with loyalty points (340 - 3,200 points) |
| **Promotions** | 3 | Active promotions (percentage discount, buy-X-get-Y) |

## Test Users (Keycloak)

These users are pre-configured in Keycloak (managed by `keycloak-config/fivetpro-realm.json`):

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| `admin` | `admin123` | Admin | Full system access |
| `manager` | `manager123` | Manager | Read access + limited write |
| `salesstaff` | `sales123` | SalesStaff | Orders, customers, payments |
| `warehousestaff` | `warehouse123` | WarehouseStaff | Stock, inventory, suppliers |

## Modular Design

Each SQL file is **independent and reusable**:
- Want only products? Run `03_products.sql` directly
- Need to reset customers? Run `05_customers.sql`
- All modules use `ON CONFLICT DO NOTHING` for idempotency

### Running Individual Modules

```bash
# Via Docker
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < 03_products.sql

# Via psql CLI
psql -U postgres -d fivetpromart_db -f 03_products.sql
```

## Integration with dev.bat

The main dev.bat now supports automatic seeding:

```bash
# Start dev mode with seeding prompt
dev.bat

# Skip seeding (default behavior)
dev.bat --no-seed

# Auto-seed without prompt
dev.bat --seed
```

## Advanced Usage

### Reset Only One Entity

```bash
# Reset products only
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "DELETE FROM products;"
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < 03_products.sql
```

### Check Seeded Data

```bash
# Count all entities
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "
SELECT 'Categories' as entity, COUNT(*) FROM product_categories
UNION ALL SELECT 'Products', COUNT(*) FROM products
UNION ALL SELECT 'Customers', COUNT(*) FROM customers
UNION ALL SELECT 'Stock Lots', COUNT(*) FROM stock_inventories
UNION ALL SELECT 'Promotions', COUNT(*) FROM promotions;
"
```

### Customize Seed Data

1. Edit the relevant SQL file (e.g., `03_products.sql`)
2. Add/modify INSERT statements
3. Run `seed-only.bat` or the specific module

## Troubleshooting

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
