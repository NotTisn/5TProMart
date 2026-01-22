-- =============================================================================
-- 5TPROMART - Seed Data: Product Categories
-- =============================================================================
-- Part 1 of modular seed system
-- =============================================================================

\echo '→ Seeding Categories...'

INSERT INTO product_categories (category_id, category_name, is_active) VALUES
    ('cat-001', 'Electronics', true),
    ('cat-002', 'Groceries & Food', true),
    ('cat-003', 'Beverages', true),
    ('cat-004', 'Personal Care', true),
    ('cat-005', 'Home & Kitchen', true),
    ('cat-006', 'Stationery', true),
    ('cat-007', 'Snacks & Candy', true),
    ('cat-008', 'Dairy Products', true),
    ('cat-009', 'Cleaning Supplies', true),
    ('cat-010', 'Health & Wellness', true)
ON CONFLICT (category_id) DO NOTHING;

\echo '  ✓ Categories seeded (10 items)'
