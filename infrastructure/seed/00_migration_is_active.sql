-- =============================================================================
-- 5TPROMART - Migration: Fix is_active columns
-- =============================================================================
-- This script fixes NULL values in is_active columns that prevent Hibernate DDL
-- Run ONCE if you have existing data without is_active values
-- =============================================================================

\echo '→ Fixing is_active columns...'

-- Step 1: Add columns if they don't exist (with default TRUE)
DO $$
BEGIN
    -- customers
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'customers' AND column_name = 'is_active') THEN
        ALTER TABLE customers ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
    END IF;
    
    -- product_categories
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'product_categories' AND column_name = 'is_active') THEN
        ALTER TABLE product_categories ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
    END IF;
    
    -- products
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'products' AND column_name = 'is_active') THEN
        ALTER TABLE products ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
    END IF;
    
    -- promotions
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'promotions' AND column_name = 'is_active') THEN
        ALTER TABLE promotions ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
    END IF;
    
    -- suppliers
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_name = 'suppliers' AND column_name = 'is_active') THEN
        ALTER TABLE suppliers ADD COLUMN is_active BOOLEAN DEFAULT TRUE;
    END IF;
END $$;

-- Step 2: Set NULL values to TRUE
UPDATE customers SET is_active = TRUE WHERE is_active IS NULL;
UPDATE product_categories SET is_active = TRUE WHERE is_active IS NULL;
UPDATE products SET is_active = TRUE WHERE is_active IS NULL;
UPDATE promotions SET is_active = TRUE WHERE is_active IS NULL;
UPDATE suppliers SET is_active = TRUE WHERE is_active IS NULL;

-- Step 3: Add NOT NULL constraint (if not already present)
ALTER TABLE customers ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE customers ALTER COLUMN is_active SET DEFAULT TRUE;

ALTER TABLE product_categories ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE product_categories ALTER COLUMN is_active SET DEFAULT TRUE;

ALTER TABLE products ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE products ALTER COLUMN is_active SET DEFAULT TRUE;

ALTER TABLE promotions ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE promotions ALTER COLUMN is_active SET DEFAULT TRUE;

ALTER TABLE suppliers ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE suppliers ALTER COLUMN is_active SET DEFAULT TRUE;

\echo '  ✓ is_active columns fixed for all tables'
