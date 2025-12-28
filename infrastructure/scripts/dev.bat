@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - DEV MODE ORCHESTRATOR
REM ============================================================================
REM Usage: dev.bat [--skip-app]
REM
REM --skip-app   Only start infrastructure, skip Spring Boot application
REM ============================================================================
REM Philosophy: "Just type dev and everything works."
REM
REM What this script does:
REM   1. Checks Docker is running
REM   2. Starts PostgreSQL + Keycloak if not already running
REM   3. Waits for infrastructure to be healthy
REM   4. Starts Spring Boot with hot reload
REM ============================================================================

echo.
echo  ============================================
echo   5TPROMART - Dev Mode (Hot Reload)
echo  ============================================
echo.

set "SCRIPT_DIR=%~dp0"
set "INFRA_DIR=%SCRIPT_DIR%.."
set "ROOT=%SCRIPT_DIR%..\.."

REM Parse arguments
set "SKIP_APP=0"
if /i "%~1"=="--skip-app" set "SKIP_APP=1"

REM ============================================================================
REM Step 0: Prerequisites Check
REM ============================================================================

echo [0/3] Checking prerequisites...

REM Check Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo  [ERROR] Docker is not running!
    echo.
    echo  Please start Docker Desktop first, then run this script again.
    echo.
    pause
    exit /b 1
)
echo       Docker: OK
echo.

REM ============================================================================
REM Step 1: Infrastructure (PostgreSQL + Keycloak)
REM ============================================================================

echo [1/3] Infrastructure (PostgreSQL + Keycloak)...

REM Check if PostgreSQL is already running on port 5432
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       Starting Docker services...
    pushd "%INFRA_DIR%"
    docker compose -f compose-infra-only.yaml up -d
    popd
    
    REM Wait for PostgreSQL to be ready
    echo       Waiting for PostgreSQL...
:wait_postgres
    timeout /t 2 /nobreak >nul
    powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_postgres
    echo       PostgreSQL: Ready
    
    REM Wait for Keycloak to be ready (takes longer)
    echo       Waiting for Keycloak (this may take 30-60 seconds on first start)...
:wait_keycloak
    timeout /t 5 /nobreak >nul
    powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/health/ready' -UseBasicParsing -TimeoutSec 5;if($r.Content -match 'UP'){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_keycloak
    echo       Keycloak: Ready
) else (
    echo       Already running (skipping Docker startup)
)

echo       Infrastructure: OK
echo.

REM ============================================================================
REM Step 2: Verify Keycloak Realm (Optional but helpful)
REM ============================================================================

echo [2/3] Verifying Keycloak realm...

REM Quick check if the realm endpoint is accessible
powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/realms/fivetpro' -UseBasicParsing -TimeoutSec 5;exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       [WARN] Realm 'fivetpro' not found.
    echo              You may need to import the realm manually:
    echo              1. Go to http://localhost:8180/admin (admin/admin)
    echo              2. Import realm from keycloak-config/fivetpro-realm.json
    echo.
) else (
    echo       Realm 'fivetpro': OK
)
echo.

REM ============================================================================
REM Step 3: Spring Boot Application
REM ============================================================================

if "%SKIP_APP%"=="1" (
    echo [3/3] Skipping application (--skip-app flag)
    goto done
)

echo [3/3] Starting Spring Boot application (with hot reload)...

REM Check if already running on port 8080
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    echo       Already running on :8080
    echo       To restart, run: scripts\restart.bat api
    goto done
)

echo       Starting 5TProMart API...

pushd "%ROOT%"

REM Check if compiled classes exist
if not exist "target\classes" (
    echo       First run - compiling (this may take a minute)...
    call mvnw.cmd compile -q -DskipTests
)

REM Start Spring Boot in a new window with hot reload
start "5TProMart-API" cmd /c "mvnw.cmd spring-boot:run -q"
popd

REM Wait for app to start
echo       Waiting for application to start...
:wait_app
timeout /t 3 /nobreak >nul
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 goto wait_app

echo       Application: Ready
echo.

REM ============================================================================
REM Done!
REM ============================================================================

:done

echo.
echo  ============================================
echo   Dev Mode Active!
echo  ============================================
echo.
echo   Services:
echo     5TProMart API     http://localhost:8080
echo     Swagger UI        http://localhost:8080/swagger-ui.html (if enabled)
echo.
echo   Infrastructure:
echo     PostgreSQL        localhost:5432/fivetpromart_db
echo     Keycloak Admin    http://localhost:8180/admin (admin/admin)
echo     Keycloak Realm    http://localhost:8180/realms/fivetpro
echo.
echo   Hot Reload:
echo     Edit code in IDE, save, wait 2-3 seconds.
echo     DevTools will auto-restart the context.
echo.
echo   Helper Scripts:
echo     stop_all.bat      Stop all services (keep Docker)
echo     stop_all.bat --docker   Stop everything
echo     restart.bat api   Restart Spring Boot app
echo     clean.bat         Nuclear reset (delete all data)
echo.

endlocal
