-- =============================================================================
-- MIGRATION: Add quantity_shelf and quantity_storage columns to stock_inventories
-- Version: V20260122
-- Description: Per API Spec §5, batches track shelf (display) vs storage (warehouse) quantities
-- =============================================================================

-- Add new columns if they don't exist
ALTER TABLE stock_inventories 
ADD COLUMN IF NOT EXISTS quantity_shelf BIGINT DEFAULT 0;

ALTER TABLE stock_inventories 
ADD COLUMN IF NOT EXISTS quantity_storage BIGINT DEFAULT 0;

-- Initialize existing data:
-- Per spec, when stock is created: quantityShelf = 0, quantityStorage = stockQuantity
-- For existing data, assume all stock is in storage (not on display)
UPDATE stock_inventories 
SET 
    quantity_shelf = 0,
    quantity_storage = stock_quantity
WHERE quantity_storage = 0 OR quantity_storage IS NULL;

-- Add comment for documentation
COMMENT ON COLUMN stock_inventories.quantity_shelf IS 'Display quantity - items currently on shelf/display';
COMMENT ON COLUMN stock_inventories.quantity_storage IS 'Warehouse quantity - items currently in storage';

-- Verify: quantity_shelf + quantity_storage should equal stock_quantity
-- This constraint ensures data integrity
-- Note: Not enforcing as CHECK constraint since disposal/sales reduce stock_quantity independently

\echo '✓ Migration V20260122 complete: Added quantity_shelf and quantity_storage columns'
