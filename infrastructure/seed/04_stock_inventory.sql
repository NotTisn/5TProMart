-- =============================================================================
-- 5TPROMART - Seed Data: Stock Inventory (Batches/Lots)
-- =============================================================================
-- Part 4 of modular seed system
-- =============================================================================

\echo '→ Seeding Stock Inventory...'

INSERT INTO stock_inventories (
    lot_id,
    product_id,
    manufacture_date,
    expiration_date,
    stock_quantity,
    reserved_quantity,
    import_price,
    status,
    version
) VALUES
    -- Electronics (no expiry)
    ('lot-001', 'prod-001', '2025-01-01', NULL, 150, 0, 120000, 'AVAILABLE', 0),
    ('lot-002', 'prod-002', '2025-01-05', NULL, 300, 0, 28000, 'AVAILABLE', 0),
    ('lot-003', 'prod-003', '2025-01-03', NULL, 200, 0, 68000, 'AVAILABLE', 0),
    
    -- Groceries & Food (with expiry)
    ('lot-004', 'prod-004', '2024-12-01', '2026-12-01', 500, 0, 105000, 'AVAILABLE', 0),
    ('lot-005', 'prod-005', '2025-01-10', '2027-01-10', 400, 0, 22000, 'AVAILABLE', 0),
    ('lot-006', 'prod-006', '2024-11-15', '2026-11-15', 250, 0, 38000, 'AVAILABLE', 0),
    ('lot-007', 'prod-007', '2025-01-08', '2025-07-08', 600, 0, 18000, 'AVAILABLE', 0),
    ('lot-008', 'prod-008', '2024-10-20', '2027-10-20', 180, 0, 26000, 'AVAILABLE', 0),
    
    -- Beverages (with expiry)
    ('lot-009', 'prod-009', '2025-01-12', '2025-12-12', 1000, 0, 9500, 'AVAILABLE', 0),
    ('lot-010', 'prod-010', '2025-01-15', '2026-01-15', 2000, 0, 4500, 'AVAILABLE', 0),
    ('lot-011', 'prod-011', '2025-01-10', '2025-04-10', 300, 0, 28000, 'AVAILABLE', 0),
    ('lot-012', 'prod-012', '2024-12-01', '2026-12-01', 250, 0, 48000, 'AVAILABLE', 0),
    
    -- Dairy Products (short expiry)
    ('lot-013', 'prod-013', '2026-01-17', '2026-01-24', 200, 0, 26000, 'AVAILABLE', 0),
    ('lot-014', 'prod-014', '2026-01-16', '2026-01-30', 180, 0, 22000, 'AVAILABLE', 0),
    ('lot-015', 'prod-015', '2025-01-10', '2025-06-10', 100, 0, 55000, 'AVAILABLE', 0),
    
    -- Personal Care
    ('lot-016', 'prod-016', '2024-12-01', '2027-12-01', 120, 0, 62000, 'AVAILABLE', 0),
    ('lot-017', 'prod-017', '2024-11-15', '2027-11-15', 350, 0, 18000, 'AVAILABLE', 0),
    ('lot-018', 'prod-018', '2025-01-05', '2027-01-05', 280, 0, 33000, 'AVAILABLE', 0),
    ('lot-019', 'prod-019', '2025-01-08', '2028-01-08', 200, 0, 28000, 'AVAILABLE', 0),
    
    -- Snacks & Candy
    ('lot-020', 'prod-020', '2025-01-10', '2025-07-10', 400, 0, 14000, 'AVAILABLE', 0),
    ('lot-021', 'prod-021', '2024-12-15', '2025-12-15', 500, 0, 11000, 'AVAILABLE', 0),
    ('lot-022', 'prod-022', '2025-01-05', '2025-10-05', 300, 0, 25000, 'AVAILABLE', 0),
    
    -- Cleaning Supplies
    ('lot-023', 'prod-023', '2024-11-01', '2027-11-01', 150, 0, 28000, 'AVAILABLE', 0),
    ('lot-024', 'prod-024', '2024-12-10', '2027-12-10', 200, 0, 42000, 'AVAILABLE', 0),
    ('lot-025', 'prod-025', '2025-01-01', NULL, 180, 0, 22000, 'AVAILABLE', 0),
    
    -- Stationery (no expiry)
    ('lot-026', 'prod-026', '2024-12-01', NULL, 250, 0, 19000, 'AVAILABLE', 0),
    ('lot-027', 'prod-027', '2024-11-20', NULL, 100, 0, 72000, 'AVAILABLE', 0),
    ('lot-028', 'prod-028', '2025-01-05', NULL, 200, 0, 25000, 'AVAILABLE', 0),
    
    -- Home & Kitchen
    ('lot-029', 'prod-029', '2024-12-15', NULL, 80, 0, 36000, 'AVAILABLE', 0),
    ('lot-030', 'prod-030', '2025-01-01', NULL, 150, 0, 30000, 'AVAILABLE', 0)
ON CONFLICT (lot_id) DO NOTHING;

-- Update product total stock quantities based on inventory
UPDATE products SET total_stock_quantity = (
    SELECT COALESCE(SUM(stock_quantity), 0)
    FROM stock_inventories
    WHERE stock_inventories.product_id = products.product_id
);

\echo '  ✓ Stock Inventory seeded (30 lots)'
\echo '  ✓ Product stock quantities updated'
