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
<<<<<<< Updated upstream
INSERT INTO supplier (id, name, contact_email, phone, address, created_at, updated_at) VALUES
    (1, 'Tech Supplier Co.', 'contact@techsupplier.com', '0901234567', '123 Tech Street, HCMC', NOW(), NOW()),
    (2, 'Fresh Foods Inc.', 'orders@freshfoods.com', '0907654321', '456 Food Ave, Hanoi', NOW(), NOW()),
    (3, 'Fashion Forward', 'hello@fashionforward.com', '0909876543', '789 Style Blvd, HCMC', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

SELECT setval('supplier_id_seq', (SELECT MAX(id) FROM supplier));

-- =============================================================================
-- Products
-- =============================================================================
INSERT INTO product (id, name, description, price, stock_quantity, category_id, supplier_id, created_at, updated_at) VALUES
    (1, 'Laptop Pro 15', 'High-performance laptop for professionals', 25000000, 50, 1, 1, NOW(), NOW()),
    (2, 'Wireless Mouse', 'Ergonomic wireless mouse', 500000, 200, 1, 1, NOW(), NOW()),
    (3, 'Organic Rice 5kg', 'Premium organic jasmine rice', 150000, 500, 2, 2, NOW(), NOW()),
    (4, 'T-Shirt Classic', 'Cotton classic fit t-shirt', 250000, 300, 3, 3, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

SELECT setval('product_id_seq', (SELECT MAX(id) FROM product));

-- =============================================================================
-- Done
-- =============================================================================
DO $$
BEGIN
    RAISE NOTICE 'Seed data inserted successfully!';
END $$;
=======
INSERT INTO products (product_id, product_name, category_id, unit_of_measure, selling_price, total_stock_quantity, created_at, updated_at, is_active) VALUES
    ('prod-001', 'USB Flash Drive 32GB', 'cat-001', 'piece', 150000, 0, NOW(), NOW(), true),
    ('prod-002', 'AA Batteries (4-pack)', 'cat-001', 'pack', 35000, 0, NOW(), NOW(), true),
    ('prod-003', 'Jasmine Rice 5kg', 'cat-002', 'bag', 125000, 0, NOW(), NOW(), true),
    ('prod-004', 'Coca-Cola 330ml', 'cat-003', 'can', 12000, 0, NOW(), NOW(), true)
ON CONFLICT (product_id) DO NOTHING;
>>>>>>> Stashed changes
