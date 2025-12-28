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
REM   all        Everything (same as stop_all + dev)
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

REM ============================================================================
REM Restart API
REM ============================================================================

if /i "%SERVICE%"=="api" (
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
:wait_api
    timeout /t 2 /nobreak >nul
    powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_api
    
    echo.
    echo  API restarted: http://localhost:8080
    echo.
    goto done
)

REM ============================================================================
REM Restart PostgreSQL
REM ============================================================================

if /i "%SERVICE%"=="postgres" (
    echo.
    echo  Restarting PostgreSQL...
    echo.
    
    pushd "%INFRA_DIR%"
    docker compose -f compose-infra-only.yaml restart postgres
    popd
    
    echo.
    echo  PostgreSQL restarted: localhost:5432
    echo.
    goto done
)

REM ============================================================================
REM Restart Keycloak
REM ============================================================================

if /i "%SERVICE%"=="keycloak" (
    echo.
    echo  Restarting Keycloak...
    echo.
    
    pushd "%INFRA_DIR%"
    docker compose -f compose-infra-only.yaml restart keycloak
    popd
    
    REM Wait for ready
    echo  Waiting for Keycloak to be ready...
:wait_kc
    timeout /t 5 /nobreak >nul
    powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/health/ready' -UseBasicParsing -TimeoutSec 5;if($r.Content -match 'UP'){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
    if errorlevel 1 goto wait_kc
    
    echo.
    echo  Keycloak restarted: http://localhost:8180/admin
    echo.
    goto done
)

REM ============================================================================
REM Restart All
REM ============================================================================

if /i "%SERVICE%"=="all" (
    echo.
    echo  Restarting everything...
    echo.
    
    call "%SCRIPT_DIR%stop_all.bat" --docker
    call "%SCRIPT_DIR%dev.bat"
    
    goto done
)

REM ============================================================================
REM Unknown Service
REM ============================================================================

echo.
echo  [ERROR] Unknown service: %SERVICE%
echo.
echo  Available services: api, postgres, keycloak, all
echo.
exit /b 1

:done
endlocal
