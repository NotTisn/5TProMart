@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - NUCLEAR RESET
REM ============================================================================
REM This script will:
REM   1. Stop ALL Java processes
REM   2. Stop and REMOVE Docker containers
REM   3. DELETE all data volumes (PostgreSQL, Keycloak)
REM   4. DELETE Maven target directories
REM
REM Use when:
REM   - State is corrupted
REM   - Starting fresh
REM   - "Just nuke it and start over"
REM ============================================================================

set "SCRIPT_DIR=%~dp0"
set "INFRA_DIR=%SCRIPT_DIR%.."
set "ROOT=%SCRIPT_DIR%..\.."

echo.
echo  ============================================
echo   NUCLEAR RESET WARNING
echo  ============================================
echo.
echo   This will DELETE:
echo     - All running Java processes
echo     - Docker containers
echo     - PostgreSQL data (all databases!)
echo     - Keycloak data (all users/realms!)
echo     - Maven target directories
echo.
echo   This action CANNOT be undone!
echo.

set /p confirm="  Type 'yes' to confirm: "
if /i not "%confirm%"=="yes" (
    echo.
    echo  Aborted.
    echo.
    exit /b 0
)

echo.
echo  Starting nuclear reset...
echo.

REM ============================================================================
REM Step 1: Kill ALL Java processes
REM ============================================================================

echo [1/4] Killing Java processes...
taskkill /F /IM java.exe /T >nul 2>&1
echo       Done.

REM ============================================================================
REM Step 2: Stop Docker with volumes
REM ============================================================================

echo [2/4] Stopping Docker and removing volumes...
pushd "%INFRA_DIR%"
docker compose -f compose-infra-only.yaml down -v >nul 2>&1
docker compose -f compose.yaml down -v >nul 2>&1
popd
echo       Done.

REM ============================================================================
REM Step 3: Delete data directories
REM ============================================================================

echo [3/4] Deleting data directories...

if exist "%INFRA_DIR%\data\postgres" (
    rmdir /s /q "%INFRA_DIR%\data\postgres"
    echo       Deleted: data\postgres
)

if exist "%INFRA_DIR%\data\keycloak" (
    rmdir /s /q "%INFRA_DIR%\data\keycloak"
    echo       Deleted: data\keycloak
)

REM ============================================================================
REM Step 4: Delete target directories
REM ============================================================================

echo [4/4] Deleting Maven build...

if exist "%ROOT%\target" (
    rmdir /s /q "%ROOT%\target"
    echo       Deleted: target
)

echo.
echo  ============================================
echo   Nuclear reset complete!
echo  ============================================
echo.
echo   To start fresh, run: dev
echo.

endlocal
