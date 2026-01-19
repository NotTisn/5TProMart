-- =============================================================================
-- 5TPROMART - DEPRECATED: Use master_seed.sql instead
-- =============================================================================
-- This file is kept for backward compatibility but is no longer maintained.
-- 
-- NEW MODULAR SYSTEM:
--   • Run master_seed.sql for complete seeding
--   • Or run individual modules (01_categories.sql, 02_suppliers.sql, etc.)
--   • Use seed-only.bat for easy Windows execution
--
-- See README.md in this folder for full documentation.
-- =============================================================================

\echo ''
\echo '⚠ WARNING: This file is deprecated!'
\echo 'Please use master_seed.sql or seed-only.bat instead.'
\echo ''
\echo 'Quick start:'
\echo '  • Windows: seed-only.bat'
\echo '  • Manual:  docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < master_seed.sql'
\echo ''

-- Legacy data below (may be outdated)
-- For current seed data, see the 0X_*.sql modules

-- =============================================================================
-- Categories
-- =============================================================================
INSERT INTO product_categories (category_id, category_name) VALUES
    ('cat-001', 'Electronics'),
    ('cat-002', 'Groceries & Food'),
    ('cat-003', 'Beverages'),
    ('cat-004', 'Personal Care')
ON CONFLICT (category_id) DO NOTHING;

-- =============================================================================
-- Products (Minimal for backward compatibility)
-- =============================================================================
INSERT INTO products (product_id, product_name, category_id, unit_of_measure, selling_price, total_stock_quantity, created_at, updated_at) VALUES
    ('prod-001', 'USB Flash Drive 32GB', 'cat-001', 'piece', 150000, 0, NOW(), NOW()),
    ('prod-002', 'AA Batteries (4-pack)', 'cat-001', 'pack', 35000, 0, NOW(), NOW()),
    ('prod-003', 'Jasmine Rice 5kg', 'cat-002', 'bag', 125000, 0, NOW(), NOW()),
    ('prod-004', 'Coca-Cola 330ml', 'cat-003', 'can', 12000, 0, NOW(), NOW())
ON CONFLICT (product_id) DO NOTHING;
