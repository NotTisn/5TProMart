-- =============================================================================
-- 5TPROMART - Seed Data: Suppliers
-- =============================================================================
-- Part 2 of modular seed system
-- =============================================================================

\echo '→ Seeding Suppliers...'

INSERT INTO suppliers (
    supplier_id, 
    supplier_name, 
    address, 
    phone_number, 
    represent_name, 
    represent_phone_number, 
    supplier_type,
    current_debt,
    is_active
) VALUES
    (
        'sup-001', 
        'Tech Electronics Co., Ltd.', 
        '123 Nguyen Hue, District 1, HCMC', 
        '0283456789',
        'Nguyen Van A',
        '0901234567',
        'ELECTRONICS',
        0.00,
        true
    ),
    (
        'sup-002', 
        'Fresh Farm Foods', 
        '456 Le Loi, District 3, HCMC', 
        '0287654321',
        'Tran Thi B',
        '0907654321',
        'FOOD',
        0.00,
        true
    ),
    (
        'sup-003', 
        'Vinamilk Distribution', 
        '789 Cach Mang Thang 8, District 10, HCMC', 
        '0289876543',
        'Le Van C',
        '0909876543',
        'DAIRY',
        0.00,
        true
    ),
    (
        'sup-004', 
        'Global Beverages Ltd.', 
        '321 Tran Hung Dao, District 5, HCMC', 
        '0281122334',
        'Pham Thi D',
        '0903334455',
        'BEVERAGES',
        0.00,
        true
    ),
    (
        'sup-005', 
        'Unilever Vietnam', 
        '654 Dien Bien Phu, Binh Thanh, HCMC', 
        '0285566778',
        'Hoang Van E',
        '0906677889',
        'PERSONAL_CARE',
        0.00,
        true
    )
ON CONFLICT (supplier_id) DO NOTHING;

\echo '  ✓ Suppliers seeded (5 items)'
