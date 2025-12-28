@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - RESTART A SINGLE SERVICE
REM ============================================================================
REM Usage: restart.bat <service>
REM
REM Services:
REM   api        Spring Boot application (port 8080)
REM   postgres   PostgreSQL database (Docker)
REM   keycloak   Keycloak identity provider (Docker)
REM   all        Everything (same as dev --stop --docker + dev)
REM ============================================================================

set "SCRIPT_DIR=%~dp0"
set "INFRA_DIR=%SCRIPT_DIR%.."
set "ROOT=%SCRIPT_DIR%..\.."

if "%~1"=="" (
    echo.
    echo  Usage: restart.bat ^<service^>
    echo.
    echo  Services:
    echo    api        Spring Boot application
    echo    postgres   PostgreSQL (Docker)
    echo    keycloak   Keycloak (Docker)
    echo    all        Everything
    echo.
    exit /b 1
)

set "SERVICE=%~1"

REM Route to appropriate handler
if /i "%SERVICE%"=="api" goto restart_api
if /i "%SERVICE%"=="postgres" goto restart_postgres
if /i "%SERVICE%"=="keycloak" goto restart_keycloak
if /i "%SERVICE%"=="all" goto restart_all

REM Unknown service
echo.
echo  [ERROR] Unknown service: %SERVICE%
echo.
echo  Available services: api, postgres, keycloak, all
echo.
exit /b 1

REM ============================================================================
REM Restart API
REM ============================================================================

:restart_api
echo.
echo  Restarting 5TProMart API...
echo.

REM Kill by port
for /f "tokens=5" %%a in ('netstat -ano 2^>nul ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo  Stopping current instance (PID %%a)...
    taskkill /F /PID %%a >nul 2>&1
)

REM Wait a moment
timeout /t 2 /nobreak >nul

REM Start new instance
pushd "%ROOT%"
echo  Starting new instance...
start "5TProMart-API" cmd /c "mvnw.cmd spring-boot:run -q"
popd

REM Wait for ready
echo  Waiting for startup...
call :wait_for_port 8080 60
if errorlevel 1 (
    echo  [ERROR] API failed to start in 60s
    exit /b 1
)

echo.
echo  API restarted: http://localhost:8080
echo.
goto :eof

REM ============================================================================
REM Restart PostgreSQL
REM ============================================================================

:restart_postgres
echo.
echo  Restarting PostgreSQL...
echo.

pushd "%INFRA_DIR%"
docker compose -f compose-infra-only.yaml restart postgres
popd

REM Wait for ready
call :wait_for_port 5432 30
if errorlevel 1 (
    echo  [ERROR] PostgreSQL failed to restart
    exit /b 1
)

echo.
echo  PostgreSQL restarted: localhost:5432
echo.
goto :eof

REM ============================================================================
REM Restart Keycloak
REM ============================================================================

:restart_keycloak
echo.
echo  Restarting Keycloak...
echo.

pushd "%INFRA_DIR%"
docker compose -f compose-infra-only.yaml restart keycloak
popd

REM Wait for ready
echo  Waiting for Keycloak to be ready...
call :wait_for_keycloak 120
if errorlevel 1 (
    echo  [ERROR] Keycloak failed to restart
    exit /b 1
)

echo.
echo  Keycloak restarted: http://localhost:8180/admin
echo.
goto :eof

REM ============================================================================
REM Restart All
REM ============================================================================

:restart_all
echo.
echo  Restarting everything...
echo.

call "%SCRIPT_DIR%stop_all.bat" --docker
call "%ROOT%dev.bat"

goto :eof

REM ============================================================================
REM FUNCTIONS
REM ============================================================================

:wait_for_port
set "wfp_port=%~1"
set "wfp_timeout=%~2"
set "wfp_elapsed=0"

:wfp_loop
if %wfp_elapsed% geq %wfp_timeout% exit /b 1
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',%wfp_port%);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 exit /b 0
timeout /t 2 /nobreak >nul
set /a wfp_elapsed+=2
goto wfp_loop

:wait_for_keycloak
set "wfk_timeout=%~1"
set "wfk_elapsed=0"

:wfk_loop
if %wfk_elapsed% geq %wfk_timeout% exit /b 1
powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/health/ready' -UseBasicParsing -TimeoutSec 5;if($r.Content -match 'UP'){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
if not errorlevel 1 exit /b 0
timeout /t 5 /nobreak >nul
set /a wfk_elapsed+=5
goto wfk_loop
