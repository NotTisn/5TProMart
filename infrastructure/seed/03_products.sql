-- =============================================================================
-- 5TPROMART - Seed Data: Products
-- =============================================================================
-- Part 3 of modular seed system
-- =============================================================================

\echo '→ Seeding Products...'

INSERT INTO products (
    product_id, 
    product_name, 
    category_id, 
    unit_of_measure, 
    selling_price, 
    total_stock_quantity
) VALUES
    -- Electronics
    ('prod-001', 'USB Flash Drive 32GB', 'cat-001', 'piece', 150000, 0),
    ('prod-002', 'AA Batteries (4-pack)', 'cat-001', 'pack', 35000, 0),
    ('prod-003', 'Phone Charger Cable', 'cat-001', 'piece', 85000, 0),
    
    -- Groceries & Food
    ('prod-004', 'Jasmine Rice 5kg', 'cat-002', 'bag', 125000, 0),
    ('prod-005', 'White Sugar 1kg', 'cat-002', 'bag', 28000, 0),
    ('prod-006', 'Cooking Oil 1L', 'cat-002', 'bottle', 45000, 0),
    ('prod-007', 'Instant Noodles (5-pack)', 'cat-002', 'pack', 22000, 0),
    ('prod-008', 'Canned Tuna 170g', 'cat-002', 'can', 32000, 0),
    
    -- Beverages
    ('prod-009', 'Coca-Cola 330ml', 'cat-003', 'can', 12000, 0),
    ('prod-010', 'Bottled Water 500ml', 'cat-003', 'bottle', 6000, 0),
    ('prod-011', 'Orange Juice 1L', 'cat-003', 'carton', 35000, 0),
    ('prod-012', 'Coffee 3-in-1 (20 sachets)', 'cat-003', 'box', 58000, 0),
    
    -- Dairy Products
    ('prod-013', 'Fresh Milk 1L', 'cat-008', 'carton', 32000, 0),
    ('prod-014', 'Yogurt 4-pack', 'cat-008', 'pack', 28000, 0),
    ('prod-015', 'Cheese Slices 200g', 'cat-008', 'pack', 68000, 0),
    
    -- Personal Care
    ('prod-016', 'Shampoo 400ml', 'cat-004', 'bottle', 78000, 0),
    ('prod-017', 'Body Soap Bar', 'cat-004', 'piece', 25000, 0),
    ('prod-018', 'Toothpaste 150g', 'cat-004', 'tube', 42000, 0),
    ('prod-019', 'Facial Tissues (3-pack)', 'cat-004', 'pack', 35000, 0),
    
    -- Snacks & Candy
    ('prod-020', 'Potato Chips 60g', 'cat-007', 'bag', 18000, 0),
    ('prod-021', 'Chocolate Bar 45g', 'cat-007', 'piece', 15000, 0),
    ('prod-022', 'Cookies 200g', 'cat-007', 'pack', 32000, 0),
    
    -- Cleaning Supplies
    ('prod-023', 'Dish Soap 500ml', 'cat-009', 'bottle', 35000, 0),
    ('prod-024', 'Laundry Detergent 800g', 'cat-009', 'bag', 52000, 0),
    ('prod-025', 'Trash Bags (20 count)', 'cat-009', 'pack', 28000, 0),
    
    -- Stationery
    ('prod-026', 'Ballpoint Pen (10-pack)', 'cat-006', 'pack', 25000, 0),
    ('prod-027', 'A4 Paper (500 sheets)', 'cat-006', 'ream', 85000, 0),
    ('prod-028', 'Notebook 200 pages', 'cat-006', 'piece', 32000, 0),
    
    -- Home & Kitchen
    ('prod-029', 'Glass Water Bottle', 'cat-005', 'piece', 45000, 0),
    ('prod-030', 'Kitchen Towel (3-pack)', 'cat-005', 'pack', 38000, 0)
ON CONFLICT (product_id) DO NOTHING;

\echo '  ✓ Products seeded (30 items)'
