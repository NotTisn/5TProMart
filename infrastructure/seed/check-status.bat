@echo off
REM ============================================================================
REM 5TPROMART - Check Seeded Data Status
REM ============================================================================
REM Quick status check for database seed data
REM ============================================================================

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║          5TPROMART - Database Status Check                     ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.

docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "
SELECT 
    'Categories' as Entity, 
    COUNT(*) as Count,
    CASE WHEN COUNT(*) >= 10 THEN '✓' ELSE '✗' END as Status
FROM product_categories
UNION ALL 
SELECT 'Products', COUNT(*), CASE WHEN COUNT(*) >= 30 THEN '✓' ELSE '✗' END
FROM products
UNION ALL 
SELECT 'Customers', COUNT(*), CASE WHEN COUNT(*) >= 15 THEN '✓' ELSE '✗' END
FROM customers
UNION ALL 
SELECT 'Stock Lots', COUNT(*), CASE WHEN COUNT(*) >= 30 THEN '✓' ELSE '✗' END
FROM stock_inventories
UNION ALL 
SELECT 'Suppliers', COUNT(*), CASE WHEN COUNT(*) >= 5 THEN '✓' ELSE '✗' END
FROM suppliers
UNION ALL 
SELECT 'Promotions', COUNT(*), CASE WHEN COUNT(*) >= 3 THEN '✓' ELSE '✗' END
FROM promotions;
" 2>nul

if errorlevel 1 (
    echo.
    echo  [ERROR] Could not connect to database!
    echo.
    echo  Possible issues:
    echo    • PostgreSQL not running: docker ps
    echo    • Wrong credentials
    echo    • Database not initialized
    echo.
    exit /b 1
)

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║  Legend:                                                       ║
echo  ║    ✓ = Expected seed data present                              ║
echo  ║    ✗ = Missing or incomplete seed data                         ║
echo  ║                                                                ║
echo  ║  To seed: seed-only.bat                                        ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.
