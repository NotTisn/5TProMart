-- =============================================================================
-- 5TPROMART - MASTER SEED SCRIPT
-- =============================================================================
-- This script runs all modular seed files in the correct order.
-- Run with: psql -U postgres -d fivetpromart_db -f master_seed.sql
-- Or via Docker: docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < master_seed.sql
-- =============================================================================

\echo ''
\echo '╔════════════════════════════════════════════════════════════════╗'
\echo '║          5TPROMART - Database Seed System                      ║'
\echo '╚════════════════════════════════════════════════════════════════╝'
\echo ''

-- Disable notices for cleaner output
SET client_min_messages TO WARNING;

-- Start transaction
BEGIN;

\echo '[Phase 1/6] Categories'
\ir 01_categories.sql

\echo ''
\echo '[Phase 2/6] Suppliers'
\ir 02_suppliers.sql

\echo ''
\echo '[Phase 3/6] Products'
\ir 03_products.sql

\echo ''
\echo '[Phase 4/6] Stock Inventory'
\ir 04_stock_inventory.sql

\echo ''
\echo '[Phase 5/6] Customers'
\ir 05_customers.sql

\echo ''
\echo '[Phase 6/6] Promotions'
\ir 06_promotions.sql

-- Commit transaction
COMMIT;

-- Re-enable notices
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
\echo '║                                                                ║'
\echo '║  Test Users (Keycloak):                                        ║'
\echo '║    • admin / admin123 (Admin)                                  ║'
\echo '║    • manager / manager123 (Manager)                            ║'
\echo '║    • salesstaff / sales123 (SalesStaff)                        ║'
\echo '║    • warehousestaff / warehouse123 (WarehouseStaff)            ║'
\echo '║                                                                ║'
\echo '║  Next Steps:                                                   ║'
\echo '║    1. Start Spring Boot: dev.bat                               ║'
\echo '║    2. Login with any test user above                           ║'
\echo '║    3. Explore products, create orders, test features!          ║'
\echo '╚════════════════════════════════════════════════════════════════╝'
\echo ''
