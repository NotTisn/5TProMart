# 5TProMart - Seed System Quick Reference

## 🚀 Most Common Commands

```bash
# Start dev mode (will prompt for seeding)
dev.bat

# Start with automatic seeding
dev.bat --seed

# Start without seeding
dev.bat --no-seed

# Seed existing database (safe, no data loss)
cd infrastructure\seed
seed-only.bat

# Reset everything and reseed (DESTRUCTIVE!)
cd infrastructure\seed
rinse-and-seed.bat
```

## 📦 What You Get

- **10** Product Categories (Electronics, Food, Beverages, etc.)
- **30** Products with realistic prices (12,000 - 150,000 VND)
- **30** Stock Inventory lots (with expiry dates)
- **15** Customers with loyalty points
- **5** Suppliers
- **3** Active Promotions

## 👤 Test Users (Keycloak)

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | Admin (full access) |
| `manager` | `manager123` | Manager |
| `salesstaff` | `sales123` | Sales Operations |
| `warehousestaff` | `warehouse123` | Inventory Management |

## 🔍 Check Seeded Data

```bash
# Count all entities
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "
SELECT 'Categories' as entity, COUNT(*) FROM product_categories
UNION ALL SELECT 'Products', COUNT(*) FROM products
UNION ALL SELECT 'Customers', COUNT(*) FROM customers
UNION ALL SELECT 'Stock Lots', COUNT(*) FROM stock_inventories
UNION ALL SELECT 'Suppliers', COUNT(*) FROM suppliers
UNION ALL SELECT 'Promotions', COUNT(*) FROM promotions;"
```

## 🛠 Troubleshooting

**"Tables don't exist"**
```bash
# Start Spring Boot first, then seed
dev.bat
# Wait for app to start, then run:
cd infrastructure\seed
seed-only.bat
```

**"Duplicate key error"**
```bash
# Data already exists, reset to start fresh:
cd infrastructure\seed
rinse-and-seed.bat
```

**"PostgreSQL not running"**
```bash
# Check Docker status
docker ps

# Start infrastructure
cd infrastructure
docker compose -f compose-infra-only.yaml up -d
```

## 📁 File Structure

```
infrastructure/seed/
├── master_seed.sql          ← Runs all modules
├── 01_categories.sql        ← Categories only
├── 02_suppliers.sql         ← Suppliers only
├── 03_products.sql          ← Products only
├── 04_stock_inventory.sql   ← Inventory only
├── 05_customers.sql         ← Customers only
├── 06_promotions.sql        ← Promotions only
├── seed-only.bat            ← Safe seeding
├── rinse-and-seed.bat       ← Reset + seed
└── README.md                ← Full docs
```

## 🎯 Pro Tips

1. **Use `--seed` flag** during development to start with data
2. **Run `seed-only.bat`** anytime to refresh test data
3. **Edit individual SQL files** to customize data
4. **Use `rinse-and-seed.bat`** for clean slate testing

## 📚 More Info

See [README.md](README.md) for complete documentation.
