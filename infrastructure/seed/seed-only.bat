@echo off
REM ============================================================================
REM 5TPROMART - SEED ONLY (No Schema Drop)
REM ============================================================================
REM This script inserts seed data into existing tables.
REM Use this when tables already exist and you just want to add test data.
REM ============================================================================

setlocal

set "SCRIPT_DIR=%~dp0"

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║          5TPROMART - Seed Database                             ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.
echo   Inserting test data into existing tables...
echo.

pushd "%SCRIPT_DIR%"
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < master_seed.sql

if errorlevel 1 (
    echo.
    echo  [ERROR] Seeding failed!
    echo.
    echo  Common issues:
    echo    • PostgreSQL not running: docker ps
    echo    • Tables don't exist: run the app first or use rinse-and-seed.bat
    echo    • Data conflicts: use rinse-and-seed.bat to start fresh
    echo.
    popd
    exit /b 1
)

popd

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║  ✓ SEED COMPLETE                                               ║
echo  ║  Database now has test data!                                   ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.
