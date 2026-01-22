-- =============================================================================
-- 5TPROMART - Seed Data: Customers
-- =============================================================================
-- Part 5 of modular seed system
-- =============================================================================

\echo '→ Seeding Customers...'

INSERT INTO customers (
    customer_id,
    full_name,
    gender,
    dob,
    phone_number,
    registration_date,
    loyalty_points,
    is_active
) VALUES
    ('cust-001', 'Nguyen Van Anh', 'Male', '1990-05-15', '0901234567', '2023-06-01', 1250, true),
    ('cust-002', 'Tran Thi Binh', 'Female', '1985-08-22', '0907654321', '2023-07-15', 2800, true),
    ('cust-003', 'Le Hoang Cuong', 'Male', '1992-12-10', '0909876543', '2023-08-20', 450, true),
    ('cust-004', 'Pham Thi Dung', 'Female', '1988-03-05', '0903334455', '2023-09-10', 3200, true),
    ('cust-005', 'Hoang Van Em', 'Male', '1995-11-18', '0906677889', '2023-10-25', 680, true),
    ('cust-006', 'Vo Thi Huong', 'Female', '1991-07-30', '0908889990', '2023-11-05', 920, true),
    ('cust-007', 'Nguyen Van Khanh', 'Male', '1987-04-12', '0905556667', '2023-12-01', 1560, true),
    ('cust-008', 'Tran Thi Lan', 'Female', '1993-09-25', '0904445556', '2024-01-15', 340, true),
    ('cust-009', 'Le Van Minh', 'Male', '1989-06-08', '0902223334', '2024-02-20', 2100, true),
    ('cust-010', 'Pham Thi Nga', 'Female', '1994-01-14', '0901112223', '2024-03-10', 780, true),
    ('cust-011', 'Hoang Van Phong', 'Male', '1986-10-20', '0909998887', '2024-04-05', 450, true),
    ('cust-012', 'Vo Thi Quynh', 'Female', '1992-02-28', '0908887776', '2024-05-12', 1650, true),
    ('cust-013', 'Nguyen Van Son', 'Male', '1991-08-16', '0907776665', '2024-06-18', 890, true),
    ('cust-014', 'Tran Thi Tuyet', 'Female', '1990-12-03', '0906665554', '2024-07-22', 2300, true),
    ('cust-015', 'Le Van Uyen', 'Male', '1988-05-09', '0905554443', '2024-08-30', 560, true)
ON CONFLICT (customer_id) DO NOTHING;

\echo '  ✓ Customers seeded (15 items)'
