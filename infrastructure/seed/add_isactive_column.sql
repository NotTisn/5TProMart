-- Migration: Add isActive column to all tables for soft delete functionality
-- Date: 2026-01-21
-- Description: Add isActive boolean column with default value TRUE to enable soft delete

-- Add isActive to product_categories table
ALTER TABLE product_categories ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to products table
ALTER TABLE products ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to suppliers table
ALTER TABLE suppliers ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to customers table
ALTER TABLE customers ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to expenses table
ALTER TABLE expenses ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to promotions table
ALTER TABLE promotions ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to staff_profiles table
ALTER TABLE staff_profiles ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to work_shifts table
ALTER TABLE work_shifts ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Add isActive to shift_role_configs table
ALTER TABLE shift_role_configs ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE NOT NULL;

-- Create indexes for better query performance on isActive column
CREATE INDEX IF NOT EXISTS idx_product_categories_is_active ON product_categories(is_active);
CREATE INDEX IF NOT EXISTS idx_products_is_active ON products(is_active);
CREATE INDEX IF NOT EXISTS idx_suppliers_is_active ON suppliers(is_active);
CREATE INDEX IF NOT EXISTS idx_customers_is_active ON customers(is_active);
CREATE INDEX IF NOT EXISTS idx_expenses_is_active ON expenses(is_active);
CREATE INDEX IF NOT EXISTS idx_promotions_is_active ON promotions(is_active);
CREATE INDEX IF NOT EXISTS idx_staff_profiles_is_active ON staff_profiles(is_active);
CREATE INDEX IF NOT EXISTS idx_work_shifts_is_active ON work_shifts(is_active);
CREATE INDEX IF NOT EXISTS idx_shift_role_configs_is_active ON shift_role_configs(is_active);

-- Update all existing records to be active
UPDATE product_categories SET is_active = TRUE WHERE is_active IS NULL;
UPDATE products SET is_active = TRUE WHERE is_active IS NULL;
UPDATE suppliers SET is_active = TRUE WHERE is_active IS NULL;
UPDATE customers SET is_active = TRUE WHERE is_active IS NULL;
UPDATE expenses SET is_active = TRUE WHERE is_active IS NULL;
UPDATE promotions SET is_active = TRUE WHERE is_active IS NULL;
UPDATE staff_profiles SET is_active = TRUE WHERE is_active IS NULL;
UPDATE work_shifts SET is_active = TRUE WHERE is_active IS NULL;
UPDATE shift_role_configs SET is_active = TRUE WHERE is_active IS NULL;

COMMIT;
