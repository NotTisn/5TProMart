-- =============================================================================
-- 5TPROMART - Seed Data: Product Categories
-- =============================================================================
-- Part 1 of modular seed system
-- =============================================================================

\echo '→ Seeding Categories...'

INSERT INTO product_categories (category_id, category_name) VALUES
    ('cat-001', 'Electronics'),
    ('cat-002', 'Groceries & Food'),
    ('cat-003', 'Beverages'),
    ('cat-004', 'Personal Care'),
    ('cat-005', 'Home & Kitchen'),
    ('cat-006', 'Stationery'),
    ('cat-007', 'Snacks & Candy'),
    ('cat-008', 'Dairy Products'),
    ('cat-009', 'Cleaning Supplies'),
    ('cat-010', 'Health & Wellness')
ON CONFLICT (category_id) DO NOTHING;

\echo '  ✓ Categories seeded (10 items)'
