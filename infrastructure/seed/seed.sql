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
INSERT INTO category (id, name, description, created_at, updated_at) VALUES
    (1, 'Electronics', 'Electronic devices and gadgets', NOW(), NOW()),
    (2, 'Groceries', 'Food and household essentials', NOW(), NOW()),
    (3, 'Clothing', 'Apparel and fashion items', NOW(), NOW()),
    (4, 'Home & Garden', 'Home improvement and garden supplies', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Reset sequence
SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));

-- =============================================================================
-- Suppliers
-- =============================================================================
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
