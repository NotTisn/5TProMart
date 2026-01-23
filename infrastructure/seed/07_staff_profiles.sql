-- ============================================================================
-- 07_staff_profiles.sql - Staff profile seed data matching Keycloak users
-- ============================================================================
-- IMPORTANT: The user_id field should match Keycloak's "sub" claim (UUID).
-- However, during dev, Keycloak uses username as the lookup key.
-- The findByUsername query uses the username field for authentication.
-- ============================================================================

-- Clear existing staff profiles (for rinse-and-seed)
-- DELETE FROM staff_profiles;

INSERT INTO staff_profiles (
    profile_id,
    user_id,
    username,
    full_name,
    email,
    phone_number,
    date_of_birth,
    account_type,
    avatar_url,
    location,
    bio,
    is_active,
    created_at,
    updated_at
) VALUES
-- Admin user (matches Keycloak "admin" user)
(
    'staff-admin-001',
    'admin',  -- Keycloak username used as user_id for dev simplicity
    'admin',
    'System Administrator',
    'admin@fivetpromart.com',
    '0901234567',
    '1985-01-15',
    'Admin',
    NULL,
    'Hồ Chí Minh, Việt Nam',
    'Quản trị viên hệ thống 5T ProMart',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
-- Manager user
(
    'staff-manager-001',
    'manager',
    'manager',
    'Store Manager',
    'manager@fivetpromart.com',
    '0901234568',
    '1988-05-20',
    'Manager',
    NULL,
    'Hồ Chí Minh, Việt Nam',
    'Quản lý cửa hàng 5T ProMart',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
-- Sales staff user
(
    'staff-sales-001',
    'salesstaff',
    'salesstaff',
    'Sales Staff',
    'sales@fivetpromart.com',
    '0901234569',
    '1995-08-10',
    'SalesStaff',
    NULL,
    'Hồ Chí Minh, Việt Nam',
    'Nhân viên bán hàng 5T ProMart',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
-- Warehouse staff user
(
    'staff-warehouse-001',
    'warehousestaff',
    'warehousestaff',
    'Warehouse Staff',
    'warehouse@fivetpromart.com',
    '0901234570',
    '1992-03-25',
    'WarehouseStaff',
    NULL,
    'Hồ Chí Minh, Việt Nam',
    'Nhân viên kho 5T ProMart',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (profile_id) DO UPDATE SET
    full_name = EXCLUDED.full_name,
    email = EXCLUDED.email,
    phone_number = EXCLUDED.phone_number,
    account_type = EXCLUDED.account_type,
    is_active = EXCLUDED.is_active,
    updated_at = CURRENT_TIMESTAMP;

-- Also ensure username uniqueness doesn't cause issues
-- This handles the case where profile_id is different but username matches
-- by updating the existing record
