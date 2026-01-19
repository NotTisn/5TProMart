@echo off
REM ============================================================================
REM 5TPROMART - RINSE AND SEED DATABASE
REM ============================================================================
REM This script:
REM   1. Drops all tables
REM   2. Recreates schema (via Hibernate on next app start)
REM   3. Waits for Spring Boot to recreate tables
REM   4. Inserts fresh seed data automatically
REM
REM WARNING: This will DELETE all data!
REM ============================================================================

setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
set "SKIP_CONFIRM=%~1"

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║          5TPROMART - Rinse and Seed Database                   ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.
echo   This will DELETE ALL DATA and insert fresh seed data.
echo.

if /i not "%SKIP_CONFIRM%"=="--yes" (
    set /p confirm="  Continue? (yes/no): "
    if /i not "!confirm!"=="yes" (
        echo.
        echo  Aborted.
        echo.
        exit /b 0
    )
)

echo.
echo  [1/4] Dropping all tables...

docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public; GRANT ALL ON SCHEMA public TO postgres; GRANT ALL ON SCHEMA public TO public;" >nul 2>&1

if errorlevel 1 (
    echo       [ERROR] Failed to drop schema! Is PostgreSQL running?
    echo       Run: docker ps
    exit /b 1
)

echo       ✓ Schema dropped

echo.
echo  [2/4] Checking if Spring Boot is running...

powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       [WARN] Spring Boot is not running on :8080
    echo       Tables will be created when you start the application.
    echo       Then run this script again to seed data, or use: seed-only.bat
    echo.
    exit /b 0
)

echo       ✓ Spring Boot detected

echo.
echo  [3/4] Waiting for Hibernate to recreate tables (15 seconds)...

timeout /t 15 /nobreak >nul

echo       ✓ Tables should be ready

echo.
echo  [4/4] Seeding data...

pushd "%SCRIPT_DIR%"
docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < master_seed.sql

if errorlevel 1 (
    echo       [ERROR] Seeding failed!
    popd
    exit /b 1
)
popd

echo.
echo  ╔════════════════════════════════════════════════════════════════╗
echo  ║  ✓ RINSE AND SEED COMPLETE                                     ║
echo  ╠════════════════════════════════════════════════════════════════╣
echo  ║  Database is now ready with fresh test data!                   ║
echo  ║                                                                ║
echo  ║  Test users (Keycloak):                                        ║
echo  ║    • admin / admin123                                          ║
echo  ║    • manager / manager123                                      ║
echo  ║    • salesstaff / sales123                                     ║
echo  ║    • warehousestaff / warehouse123                             ║
echo  ╚════════════════════════════════════════════════════════════════╝
echo.
echo   Database reset complete!
echo  ============================================
echo.
