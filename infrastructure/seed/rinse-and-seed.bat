@echo off
REM ============================================================================
REM 5TPROMART - RINSE AND SEED DATABASE
REM ============================================================================
REM This script:
REM   1. Drops all tables
REM   2. Recreates schema (via Hibernate on next app start)
REM   3. Inserts fresh seed data
REM
REM WARNING: This will DELETE all data!
REM ============================================================================

set "SCRIPT_DIR=%~dp0"

echo.
echo  ============================================
echo   Rinse and Seed Database
echo  ============================================
echo.
echo   This will DELETE ALL DATA and insert fresh seed data.
echo.

set /p confirm="  Continue? (yes/no): "
if /i not "%confirm%"=="yes" (
    echo.
    echo  Aborted.
    echo.
    exit /b 0
)

echo.
echo  [1/2] Dropping all tables...

docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db -c "
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
"

echo       Done. Tables dropped.

echo.
echo  [2/2] Note: Tables will be recreated when Spring Boot starts.
echo        Run the application, then execute seed.sql to insert data.
echo.
echo        To seed now (if tables exist):
echo        docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db ^< seed\seed.sql
echo.
echo  ============================================
echo   Database reset complete!
echo  ============================================
echo.
