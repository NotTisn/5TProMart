@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - DEV MODE
REM ============================================================================
REM Clone. Run. Code.
REM
REM Usage: dev [--skip-app] [--stop] [--status] [--clean]
REM
REM   (no args)     Start everything
REM   --skip-app    Only start infrastructure (PostgreSQL + Keycloak)
REM   --stop        Stop all services
REM   --docker      (with --stop) Also stop Docker containers
REM   --status      Show service status
REM   --clean       Nuclear reset (delete all data)
REM ============================================================================

set "ROOT=%~dp0"
set "INFRA=%ROOT%infrastructure"

REM ============================================================================
REM Parse Arguments
REM ============================================================================

if /i "%~1"=="--stop" (
    if /i "%~2"=="--docker" (
        call "%INFRA%\scripts\stop_all.bat" --docker
    ) else (
        call "%INFRA%\scripts\stop_all.bat"
    )
    goto :eof
)

if /i "%~1"=="--status" (
    call "%INFRA%\scripts\status.bat"
    goto :eof
)

if /i "%~1"=="--clean" (
    call "%INFRA%\scripts\clean.bat"
    goto :eof
)

REM ============================================================================
REM Main: Start Everything
REM ============================================================================

echo.
echo  ============================================
echo   5TPROMART - Dev Mode
echo  ============================================
echo.

REM --- Check Docker ---
docker info >nul 2>&1
if errorlevel 1 (
    echo  [ERROR] Docker is not running!
    echo          Start Docker Desktop first.
    echo.
    pause
    exit /b 1
)

REM --- Start Infrastructure ---
echo [1/3] Infrastructure (PostgreSQL + Keycloak)...

powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       Starting Docker services...
    pushd "%INFRA%"
    docker compose -f compose-infra-only.yaml up -d
    popd
    
    echo       Waiting for PostgreSQL...
:wait_pg
    timeout /t 2 /nobreak >nul
    powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_pg
    
    echo       Waiting for Keycloak (30-60s on first run)...
:wait_kc
    timeout /t 5 /nobreak >nul
    powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/health/ready' -UseBasicParsing -TimeoutSec 5;if($r.Content -match 'UP'){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_kc
) else (
    echo       Already running.
)
echo       Done.
echo.

REM --- Verify Realm ---
echo [2/3] Keycloak realm...
powershell -Command "try{Invoke-WebRequest -Uri 'http://localhost:8180/realms/fivetpro' -UseBasicParsing -TimeoutSec 5;exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       [WARN] Realm 'fivetpro' not found. May need manual import.
) else (
    echo       OK.
)
echo.

REM --- Start App ---
if /i "%~1"=="--skip-app" (
    echo [3/3] Skipping app (--skip-app)
    goto done
)

echo [3/3] Spring Boot (hot reload)...

powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    echo       Already running on :8080
    goto done
)

pushd "%ROOT%"
if not exist "target\classes" (
    echo       Compiling (first run)...
    call mvnw.cmd compile -q -DskipTests
)
start "5TProMart" cmd /c "mvnw.cmd spring-boot:run -q"
popd

echo       Waiting for startup...
:wait_app
timeout /t 3 /nobreak >nul
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 goto wait_app
echo       Ready.
echo.

:done
echo  ============================================
echo   Ready!
echo  ============================================
echo.
echo   API:        http://localhost:8080
echo   Keycloak:   http://localhost:8180/admin (admin/admin)
echo   PostgreSQL: localhost:5432 (postgres/votrungtin2005)
echo.
echo   Commands:
echo     dev --stop        Stop services
echo     dev --stop --docker  Stop everything
echo     dev --status      Check status
echo     dev --clean       Reset all data
echo.

endlocal
