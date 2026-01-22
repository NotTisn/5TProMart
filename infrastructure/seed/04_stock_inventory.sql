-- =============================================================================
-- 5TPROMART - Seed Data: Stock Inventory (Batches/Lots)
-- =============================================================================
-- Part 4 of modular seed system
-- Updated: January 2026 - All expiration dates updated to be future-dated
-- =============================================================================

\echo '→ Seeding Stock Inventory...'

INSERT INTO stock_inventories (
    lot_id,
    product_id,
    manufacture_date,
    expiration_date,
    stock_quantity,
    reserved_quantity,
    quantity_shelf,
    quantity_storage,
    import_price,
    status,
    version
) VALUES
    -- Electronics (no expiry) - Some display, rest in storage
    ('lot-001', 'prod-001', '2025-11-01', NULL, 150, 0, 10, 140, 120000, 'AVAILABLE', 0),
    ('lot-002', 'prod-002', '2025-11-05', NULL, 300, 0, 20, 280, 28000, 'AVAILABLE', 0),
    ('lot-003', 'prod-003', '2025-11-03', NULL, 200, 0, 15, 185, 68000, 'AVAILABLE', 0),
    
    -- Groceries & Food (with expiry - all future dates)
    ('lot-004', 'prod-004', '2025-10-01', '2027-10-01', 500, 0, 50, 450, 105000, 'AVAILABLE', 0),
    ('lot-005', 'prod-005', '2025-11-10', '2027-11-10', 400, 0, 30, 370, 22000, 'AVAILABLE', 0),
    ('lot-006', 'prod-006', '2025-09-15', '2027-09-15', 250, 0, 25, 225, 38000, 'AVAILABLE', 0),
    ('lot-007', 'prod-007', '2025-12-08', '2026-06-08', 600, 0, 40, 560, 18000, 'AVAILABLE', 0),
    ('lot-008', 'prod-008', '2025-08-20', '2028-08-20', 180, 0, 20, 160, 26000, 'AVAILABLE', 0),
    
    -- Beverages (with expiry - all future dates)
    ('lot-009', 'prod-009', '2025-11-12', '2026-11-12', 1000, 0, 100, 900, 9500, 'AVAILABLE', 0),
    ('lot-010', 'prod-010', '2025-12-15', '2026-12-15', 2000, 0, 200, 1800, 4500, 'AVAILABLE', 0),
    ('lot-011', 'prod-011', '2025-11-10', '2026-05-10', 300, 0, 30, 270, 28000, 'AVAILABLE', 0),
    ('lot-012', 'prod-012', '2025-10-01', '2027-10-01', 250, 0, 25, 225, 48000, 'AVAILABLE', 0),
    
    -- Dairy Products (short expiry - for testing expiring-soon)
    ('lot-013', 'prod-013', '2026-01-17', '2026-01-25', 200, 0, 50, 150, 26000, 'AVAILABLE', 0),
    ('lot-014', 'prod-014', '2026-01-16', '2026-01-28', 180, 0, 40, 140, 22000, 'AVAILABLE', 0),
    ('lot-015', 'prod-015', '2025-12-10', '2026-06-10', 100, 0, 20, 80, 55000, 'AVAILABLE', 0),
    
    -- Personal Care (long expiry)
    ('lot-016', 'prod-016', '2025-10-01', '2028-10-01', 120, 0, 15, 105, 62000, 'AVAILABLE', 0),
    ('lot-017', 'prod-017', '2025-09-15', '2028-09-15', 350, 0, 30, 320, 18000, 'AVAILABLE', 0),
    ('lot-018', 'prod-018', '2025-11-05', '2028-11-05', 280, 0, 25, 255, 33000, 'AVAILABLE', 0),
    ('lot-019', 'prod-019', '2025-11-08', '2029-11-08', 200, 0, 20, 180, 28000, 'AVAILABLE', 0),
    
    -- Snacks & Candy (medium expiry)
    ('lot-020', 'prod-020', '2025-12-10', '2026-06-10', 400, 0, 60, 340, 14000, 'AVAILABLE', 0),
    ('lot-021', 'prod-021', '2025-10-15', '2026-10-15', 500, 0, 50, 450, 11000, 'AVAILABLE', 0),
    ('lot-022', 'prod-022', '2025-11-05', '2026-08-05', 300, 0, 30, 270, 25000, 'AVAILABLE', 0),
    
    -- Cleaning Supplies (long expiry)
    ('lot-023', 'prod-023', '2025-09-01', '2028-09-01', 150, 0, 15, 135, 28000, 'AVAILABLE', 0),
    ('lot-024', 'prod-024', '2025-10-10', '2028-10-10', 200, 0, 20, 180, 42000, 'AVAILABLE', 0),
    ('lot-025', 'prod-025', '2025-11-01', NULL, 180, 0, 15, 165, 22000, 'AVAILABLE', 0),
    
    -- Stationery (no expiry)
    ('lot-026', 'prod-026', '2025-10-01', NULL, 250, 0, 25, 225, 19000, 'AVAILABLE', 0),
    ('lot-027', 'prod-027', '2025-09-20', NULL, 100, 0, 10, 90, 72000, 'AVAILABLE', 0),
    ('lot-028', 'prod-028', '2025-11-05', NULL, 200, 0, 20, 180, 25000, 'AVAILABLE', 0),
    
    -- Home & Kitchen (no expiry)
    ('lot-029', 'prod-029', '2025-10-15', NULL, 80, 0, 8, 72, 36000, 'AVAILABLE', 0),
    ('lot-030', 'prod-030', '2025-11-01', NULL, 150, 0, 15, 135, 30000, 'AVAILABLE', 0)
ON CONFLICT (lot_id) DO NOTHING;

-- Update product total stock quantities based on inventory
UPDATE products SET total_stock_quantity = (
    SELECT COALESCE(SUM(stock_quantity), 0)
    FROM stock_inventories
    WHERE stock_inventories.product_id = products.product_id
);

\echo '  ✓ Stock Inventory seeded (30 lots)'
\echo '  ✓ Product stock quantities updated'
