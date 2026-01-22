-- =============================================================================
-- 5TPROMART - CONSOLIDATED SEED SCRIPT (SCHEMA-CORRECTED)
-- =============================================================================
-- Matches actual database schema from Hibernate DDL
-- Run via: docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < consolidated_seed.sql
-- =============================================================================

\echo ''
\echo '╔════════════════════════════════════════════════════════════════╗'
\echo '║          5TPROMART - Database Seed System                      ║'
\echo '╚════════════════════════════════════════════════════════════════╝'
\echo ''

SET client_min_messages TO WARNING;
BEGIN;

-- =============================================================================
-- PHASE 1: CATEGORIES (category_id, category_name, is_active)
-- =============================================================================
\echo '[Phase 1/6] Categories'

INSERT INTO product_categories (category_id, category_name, is_active) VALUES
('cat-001', 'Electronics', true),
('cat-002', 'Groceries', true),
('cat-003', 'Beverages', true),
('cat-004', 'Snacks', true),
('cat-005', 'Personal Care', true),
('cat-006', 'Household', true),
('cat-007', 'Dairy & Eggs', true),
('cat-008', 'Bakery', true),
('cat-009', 'Frozen Foods', true),
('cat-010', 'Office Supplies', true)
ON CONFLICT (category_id) DO NOTHING;

-- =============================================================================
-- PHASE 2: SUPPLIERS (supplier_id, supplier_name, address, phone_number, represent_name, represent_phone_number, supplier_type, is_active)
-- =============================================================================
\echo ''
\echo '[Phase 2/6] Suppliers'

INSERT INTO suppliers (supplier_id, supplier_name, address, phone_number, represent_name, represent_phone_number, supplier_type, is_active) VALUES
('sup-001', 'Vinamilk Co., Ltd.', '10 Tan Trao, Tan Phu Ward, District 7, HCMC', '028-54141414', 'Nguyen Van A', '0901234567', 'Dairy', true),
('sup-002', 'Coca-Cola Vietnam', '222 Dien Bien Phu, Ward 7, District 3, HCMC', '028-39971369', 'Tran Thi B', '0912345678', 'Beverages', true),
('sup-003', 'Unilever Vietnam', '159 Hai Ba Trung, Da Kao Ward, District 1, HCMC', '028-39978888', 'Le Van C', '0923456789', 'Personal Care', true),
('sup-004', 'Samsung Vietnam', 'HCMC High-Tech Park, District 9, HCMC', '1800-588-889', 'Park Min D', '0934567890', 'Electronics', true),
('sup-005', 'Kimberly-Clark Vietnam', 'Saigon Trade Center, 37 Ton Duc Thang, District 1, HCMC', '028-39302888', 'Pham Thi E', '0945678901', 'Household', true)
ON CONFLICT (supplier_id) DO NOTHING;

-- =============================================================================
-- PHASE 3: PRODUCTS (product_id, product_name, category_id, unit_of_measure, selling_price, total_stock_quantity, is_active)
-- Note: created_at, updated_at, created_by, updated_by managed by Spring
-- =============================================================================
\echo ''
\echo '[Phase 3/6] Products'

INSERT INTO products (product_id, product_name, category_id, unit_of_measure, selling_price, total_stock_quantity, is_active) VALUES
('prod-001', 'Samsung Galaxy A54 5G', 'cat-001', 'Unit', 9990000, 0, true),
('prod-002', 'iPhone 14 128GB', 'cat-001', 'Unit', 19990000, 0, true),
('prod-003', 'Sony WH-1000XM5 Headphones', 'cat-001', 'Unit', 8990000, 0, true),
('prod-004', 'Anker PowerCore 20000mAh', 'cat-001', 'Unit', 899000, 0, true),
('prod-005', 'Gạo ST25 Túi 5kg', 'cat-002', 'Bag', 135000, 0, true),
('prod-006', 'Dầu ăn Simply 1L', 'cat-002', 'Bottle', 45000, 0, true),
('prod-007', 'Nước tương Chinsu 500ml', 'cat-002', 'Bottle', 22000, 0, true),
('prod-008', 'Mì Hảo Hảo Tôm Chua Cay Gói 75g', 'cat-004', 'Pack', 4000, 0, true),
('prod-009', 'Coca-Cola Lon 330ml', 'cat-003', 'Can', 10000, 0, true),
('prod-010', 'Nước khoáng Lavie 500ml', 'cat-003', 'Bottle', 6000, 0, true),
('prod-011', 'Sữa tươi Vinamilk 1L', 'cat-003', 'Box', 32000, 0, true),
('prod-012', 'Trà xanh C2 Không Độ 455ml', 'cat-003', 'Bottle', 10000, 0, true),
('prod-013', 'Bánh quy Cosy Marie 288g', 'cat-004', 'Pack', 28000, 0, true),
('prod-014', 'Snack Oishi Bò Nướng 42g', 'cat-004', 'Pack', 9000, 0, true),
('prod-015', 'Kẹo Alpenliebe Vị Dâu 120 Viên', 'cat-004', 'Bag', 35000, 0, true),
('prod-016', 'Dầu gội Clear Men 630ml', 'cat-005', 'Bottle', 125000, 0, true),
('prod-017', 'Xà phòng Lifebuoy 90g', 'cat-005', 'Bar', 12000, 0, true),
('prod-018', 'Kem đánh răng P/S 230g', 'cat-005', 'Tube', 42000, 0, true),
('prod-019', 'Bột giặt OMO Matic 3.7kg', 'cat-006', 'Bag', 175000, 0, true),
('prod-020', 'Nước rửa chén Sunlight 750g', 'cat-006', 'Bottle', 32000, 0, true),
('prod-021', 'Giấy vệ sinh Kleenex 10 Cuộn', 'cat-006', 'Pack', 65000, 0, true),
('prod-022', 'Sữa chua Vinamilk Có Đường 100g', 'cat-007', 'Cup', 5000, 0, true),
('prod-023', 'Phô mai Con Bò Cười 120g', 'cat-007', 'Pack', 38000, 0, true),
('prod-024', 'Trứng gà CP 10 Quả', 'cat-007', 'Tray', 35000, 0, true),
('prod-025', 'Bánh mì sandwich Kinh Đô 250g', 'cat-008', 'Loaf', 28000, 0, true),
('prod-026', 'Bánh bông lan trứng muối 240g', 'cat-008', 'Box', 45000, 0, true),
('prod-027', 'Kem Wall''s Cornetto Dâu 120ml', 'cat-009', 'Cone', 18000, 0, true),
('prod-028', 'Rau cải xanh đông lạnh 400g', 'cat-009', 'Pack', 32000, 0, true),
('prod-029', 'Bút bi Thiên Long TL-079 Xanh', 'cat-010', 'Piece', 5000, 0, true),
('prod-030', 'Sổ lò xo Campus B5 200 trang', 'cat-010', 'Book', 45000, 0, true)
ON CONFLICT (product_id) DO NOTHING;

-- =============================================================================
-- PHASE 4: STOCK INVENTORY (lot_id, product_id, stock_quantity, reserved_quantity, quantity_shelf, quantity_storage, import_price, manufacture_date, expiration_date, status, version)
-- Updated: January 2026 - All expiration dates updated to be future-dated
-- Added: quantity_shelf (display stock) and quantity_storage (warehouse stock) per API Spec §5
-- Note: created_at, updated_at, created_by, updated_by managed by Spring
-- =============================================================================
\echo ''
\echo '[Phase 4/6] Stock Inventory'

INSERT INTO stock_inventories (
    lot_id, product_id, stock_quantity, reserved_quantity, quantity_shelf, quantity_storage, import_price, 
    manufacture_date, expiration_date, status, version
) VALUES
-- Electronics (no expiry) - Some display, rest in storage
('lot-001', 'prod-001', 150, 0, 10, 140, 120000, '2025-11-01', NULL, 'AVAILABLE', 0),
('lot-002', 'prod-002', 300, 0, 20, 280, 28000, '2025-11-05', NULL, 'AVAILABLE', 0),
('lot-003', 'prod-003', 200, 0, 15, 185, 68000, '2025-11-03', NULL, 'AVAILABLE', 0),
('lot-004', 'prod-004', 100, 0, 10, 90, 75000, '2025-10-01', NULL, 'AVAILABLE', 0),

-- Groceries & Food (with future expiry)
('lot-005', 'prod-005', 400, 0, 30, 370, 22000, '2025-11-10', '2027-11-10', 'AVAILABLE', 0),
('lot-006', 'prod-006', 250, 0, 25, 225, 38000, '2025-09-15', '2027-09-15', 'AVAILABLE', 0),
('lot-007', 'prod-007', 600, 0, 40, 560, 18000, '2025-12-08', '2026-06-08', 'AVAILABLE', 0),
('lot-008', 'prod-008', 180, 0, 20, 160, 26000, '2025-08-20', '2028-08-20', 'AVAILABLE', 0),

-- Beverages (with future expiry)
('lot-009', 'prod-009', 1000, 0, 100, 900, 9500, '2025-11-12', '2026-11-12', 'AVAILABLE', 0),
('lot-010', 'prod-010', 2000, 0, 200, 1800, 4500, '2025-12-15', '2026-12-15', 'AVAILABLE', 0),
('lot-011', 'prod-011', 300, 0, 30, 270, 28000, '2025-11-10', '2026-05-10', 'AVAILABLE', 0),
('lot-012', 'prod-012', 250, 0, 25, 225, 48000, '2025-10-01', '2027-10-01', 'AVAILABLE', 0),

-- Dairy Products (short expiry - for testing expiring-soon)
('lot-013', 'prod-013', 200, 0, 50, 150, 26000, '2026-01-17', '2026-01-25', 'AVAILABLE', 0),
('lot-014', 'prod-014', 180, 0, 40, 140, 22000, '2026-01-16', '2026-01-28', 'AVAILABLE', 0),
('lot-015', 'prod-015', 100, 0, 20, 80, 55000, '2025-12-10', '2026-06-10', 'AVAILABLE', 0),

-- Personal Care (long expiry)
('lot-016', 'prod-016', 120, 0, 15, 105, 62000, '2025-10-01', '2028-10-01', 'AVAILABLE', 0),
('lot-017', 'prod-017', 350, 0, 30, 320, 18000, '2025-09-15', '2028-09-15', 'AVAILABLE', 0),
('lot-018', 'prod-018', 280, 0, 25, 255, 33000, '2025-11-05', '2028-11-05', 'AVAILABLE', 0),
('lot-019', 'prod-019', 200, 0, 20, 180, 28000, '2025-11-08', '2029-11-08', 'AVAILABLE', 0),

-- Snacks & Candy (medium expiry)
('lot-020', 'prod-020', 400, 0, 60, 340, 14000, '2025-12-10', '2026-06-10', 'AVAILABLE', 0),
('lot-021', 'prod-021', 500, 0, 50, 450, 11000, '2025-10-15', '2026-10-15', 'AVAILABLE', 0),
('lot-022', 'prod-022', 300, 0, 30, 270, 25000, '2025-11-05', '2026-08-05', 'AVAILABLE', 0),

-- Cleaning Supplies (long expiry)
('lot-023', 'prod-023', 150, 0, 15, 135, 28000, '2025-09-01', '2028-09-01', 'AVAILABLE', 0),
('lot-024', 'prod-024', 200, 0, 20, 180, 42000, '2025-10-10', '2028-10-10', 'AVAILABLE', 0),
('lot-025', 'prod-025', 180, 0, 15, 165, 22000, '2025-11-01', NULL, 'AVAILABLE', 0),

-- Stationery (no expiry)
('lot-026', 'prod-026', 250, 0, 25, 225, 19000, '2025-10-01', NULL, 'AVAILABLE', 0),
('lot-027', 'prod-027', 100, 0, 10, 90, 72000, '2025-09-20', NULL, 'AVAILABLE', 0),
('lot-028', 'prod-028', 200, 0, 20, 180, 25000, '2025-11-05', NULL, 'AVAILABLE', 0),

-- Home & Kitchen (no expiry)
('lot-029', 'prod-029', 80, 0, 8, 72, 36000, '2025-10-15', NULL, 'AVAILABLE', 0),
('lot-030', 'prod-030', 150, 0, 15, 135, 30000, '2025-11-01', NULL, 'AVAILABLE', 0)
ON CONFLICT (lot_id) DO NOTHING;

-- Update product stock quantities from inventory
UPDATE products p 
SET total_stock_quantity = (
    SELECT COALESCE(SUM(stock_quantity), 0) 
    FROM stock_inventories s 
    WHERE s.product_id = p.product_id 
      AND s.status = 'AVAILABLE'
)
WHERE EXISTS (
    SELECT 1 FROM stock_inventories s WHERE s.product_id = p.product_id
);

-- =============================================================================
-- PHASE 5: CUSTOMERS (customer_id, full_name, phone_number, loyalty_points, registration_date, dob, gender, is_active)
-- =============================================================================
\echo ''
\echo '[Phase 5/6] Customers'

INSERT INTO customers (customer_id, full_name, phone_number, loyalty_points, registration_date, dob, gender, is_active) VALUES
('cust-001', 'Nguyễn Văn An', '0901234567', 1250, '2023-01-15', '1985-03-20', 'Male', true),
('cust-002', 'Trần Thị Bình', '0912345678', 870, '2023-02-20', '1990-07-15', 'Female', true),
('cust-003', 'Lê Hoàng Châu', '0923456789', 2100, '2023-03-10', '1988-11-30', 'Male', true),
('cust-004', 'Phạm Minh Đức', '0934567890', 540, '2023-04-05', '1992-05-12', 'Male', true),
('cust-005', 'Vũ Thu Hà', '0945678901', 1680, '2023-05-18', '1987-09-25', 'Female', true),
('cust-006', 'Hoàng Văn Hùng', '0956789012', 320, '2023-06-22', '1995-02-08', 'Male', true),
('cust-007', 'Đặng Thị Lan', '0967890123', 950, '2023-07-14', '1991-12-17', 'Female', true),
('cust-008', 'Bùi Quốc Minh', '0978901234', 2450, '2023-08-09', '1986-04-22', 'Male', true),
('cust-009', 'Ngô Thị Nga', '0989012345', 1100, '2023-09-03', '1993-08-14', 'Female', true),
('cust-010', 'Trương Văn Phát', '0990123456', 670, '2023-10-11', '1989-06-05', 'Male', true),
('cust-011', 'Lý Thị Quỳnh', '0901112233', 1890, '2023-11-25', '1994-10-28', 'Female', true),
('cust-012', 'Võ Minh Sơn', '0912223344', 430, '2023-12-07', '1987-01-19', 'Male', true),
('cust-013', 'Phan Thị Tâm', '0923334455', 1560, '2024-01-15', '1992-03-11', 'Female', true),
('cust-014', 'Đỗ Văn Uyên', '0934445566', 780, '2024-02-08', '1990-07-23', 'Male', true),
('cust-015', 'Mai Thị Vân', '0945556677', 2200, '2024-03-02', '1988-11-15', 'Female', true)
ON CONFLICT (customer_id) DO NOTHING;

-- =============================================================================
-- PHASE 6: PROMOTIONS (promotion_id, promotion_name, promotion_description, promotion_type, discount_percent, buy_quantity, get_quantity, start_date, end_date, status, is_active)
-- =============================================================================
\echo ''
\echo '[Phase 6/6] Promotions'

INSERT INTO promotions (promotion_id, promotion_name, promotion_description, promotion_type, discount_percent, buy_quantity, get_quantity, start_date, end_date, status, is_active) VALUES
('promo-001', 'Flash Sale Điện Tử', 'Giảm giá sốc các sản phẩm điện tử trong 3 ngày', 'Discount', 15, NULL, NULL, CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day', 'ACTIVE', true),
('promo-002', 'Mua 2 Tặng 1', 'Áp dụng cho tất cả đồ ăn vặt', 'Buy X Get Y', 33, 2, 1, CURRENT_DATE - INTERVAL '5 days', CURRENT_DATE + INTERVAL '16 days', 'ACTIVE', true),
('promo-003', 'Khuyến Mãi Hàng Tươi Sống', 'Giảm 10% cho sữa tươi, trứng, rau đông lạnh', 'Discount', 10, NULL, NULL, CURRENT_DATE - INTERVAL '5 days', CURRENT_DATE + INTERVAL '11 days', 'ACTIVE', true)
ON CONFLICT (promotion_id) DO NOTHING;

-- Only insert promotion_products if they don't already exist (check via NOT EXISTS)
INSERT INTO promotion_products (promotion_id, product_id, product_name)
SELECT 'promo-001', 'prod-001', 'Samsung Galaxy A54 5G' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-001' AND product_id='prod-001')
UNION ALL SELECT 'promo-001', 'prod-002', 'iPhone 14 128GB' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-001' AND product_id='prod-002')
UNION ALL SELECT 'promo-001', 'prod-003', 'Sony WH-1000XM5 Headphones' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-001' AND product_id='prod-003')
UNION ALL SELECT 'promo-001', 'prod-004', 'Anker PowerCore 20000mAh' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-001' AND product_id='prod-004')
UNION ALL SELECT 'promo-002', 'prod-008', 'Mì Hảo Hảo Tôm Chua Cay Gói 75g' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-002' AND product_id='prod-008')
UNION ALL SELECT 'promo-002', 'prod-013', 'Bánh quy Cosy Marie 288g' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-002' AND product_id='prod-013')
UNION ALL SELECT 'promo-002', 'prod-014', 'Snack Oishi Bò Nướng 42g' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-002' AND product_id='prod-014')
UNION ALL SELECT 'promo-002', 'prod-015', 'Kẹo Alpenliebe Vị Dâu 120 Viên' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-002' AND product_id='prod-015')
UNION ALL SELECT 'promo-003', 'prod-011', 'Sữa tươi Vinamilk 1L' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-003' AND product_id='prod-011')
UNION ALL SELECT 'promo-003', 'prod-022', 'Sữa chua Vinamilk Có Đường 100g' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-003' AND product_id='prod-022')
UNION ALL SELECT 'promo-003', 'prod-024', 'Trứng gà CP 10 Quả' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-003' AND product_id='prod-024')
UNION ALL SELECT 'promo-003', 'prod-028', 'Rau cải xanh đông lạnh 400g' WHERE NOT EXISTS (SELECT 1 FROM promotion_products WHERE promotion_id='promo-003' AND product_id='prod-028');

COMMIT;
SET client_min_messages TO NOTICE;

\echo ''
\echo '╔════════════════════════════════════════════════════════════════╗'
\echo '║  ✓ SEED COMPLETE                                               ║'
\echo '╠════════════════════════════════════════════════════════════════╣'
\echo '║  Summary:                                                      ║'
\echo '║    • Categories: 10                                            ║'
\echo '║    • Suppliers: 5                                              ║'
\echo '║    • Products: 30 (with realistic prices)                      ║'
\echo '║    • Stock Lots: 30 (with expiry tracking)                     ║'
\echo '║    • Customers: 15 (with loyalty points)                       ║'
\echo '║    • Promotions: 3 (active campaigns)                          ║'
\echo '║                                                                 ║'
\echo '║  Test Users (Keycloak):                                        ║'
\echo '║    • admin / admin123 (Admin)                                  ║'
\echo '║    • manager / manager123 (Manager)                            ║'
\echo '║    • salesstaff / sales123 (SalesStaff)                        ║'
\echo '║    • warehousestaff / warehouse123 (WarehouseStaff)            ║'
\echo '║                                                                 ║'
\echo '║  Next Steps:                                                   ║'
\echo '║    1. Test the features with realistic data                    ║'
\echo '║    2. Login with any test user above                           ║'
\echo '║    3. Explore products, create orders, test workflows!         ║'
\echo '╚════════════════════════════════════════════════════════════════╝'
\echo ''
