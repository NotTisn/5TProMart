-- =============================================================================
-- 5TPROMART - Seed Data for Development
-- =============================================================================
-- Run with: psql -U postgres -d fivetpromart_db -f seed.sql
-- Or via Docker: docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < seed.sql
-- =============================================================================

-- Note: Hibernate will create tables via ddl-auto: update
-- This file is for inserting test data AFTER tables exist

-- =============================================================================
-- Categories
-- =============================================================================
INSERT INTO product_categories (category_id, category_name, is_active) VALUES
    ('cat-001', 'Electronics', true),
    ('cat-002', 'Groceries & Food', true),
    ('cat-003', 'Beverages', true),
    ('cat-004', 'Personal Care', true)
ON CONFLICT (category_id) DO NOTHING;

-- =============================================================================
-- Suppliers
-- =============================================================================
INSERT INTO products (product_id, product_name, category_id, unit_of_measure, selling_price, total_stock_quantity, created_at, updated_at, is_active) VALUES
    ('prod-001', 'USB Flash Drive 32GB', 'cat-001', 'piece', 150000, 0, NOW(), NOW(), true),
    ('prod-002', 'AA Batteries (4-pack)', 'cat-001', 'pack', 35000, 0, NOW(), NOW(), true),
    ('prod-003', 'Jasmine Rice 5kg', 'cat-002', 'bag', 125000, 0, NOW(), NOW(), true),
    ('prod-004', 'Coca-Cola 330ml', 'cat-003', 'can', 12000, 0, NOW(), NOW(), true)
ON CONFLICT (product_id) DO NOTHING;
