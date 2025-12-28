@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - STOP ALL SERVICES
REM ============================================================================
REM Usage: stop_all.bat [--docker]
REM
REM --docker   Also stop Docker containers (PostgreSQL, Keycloak)
REM            Without this flag, only Java processes are stopped.
REM            Docker is kept running for faster restart next time.
REM ============================================================================

echo.
echo  Stopping 5TProMart services...
echo.

set "SCRIPT_DIR=%~dp0"
set "INFRA_DIR=%SCRIPT_DIR%.."
set "ROOT=%SCRIPT_DIR%..\.."

REM ============================================================================
REM Step 1: Kill Java processes on known ports
REM ============================================================================

echo [1/2] Stopping Java services...

REM Port 8080 = Spring Boot API
for /f "tokens=5" %%a in ('netstat -ano 2^>nul ^| findstr ":8080" ^| findstr "LISTENING"') do (
    echo       Stopping port 8080 (PID %%a)
    taskkill /F /PID %%a >nul 2>&1
)

echo       Java services: Stopped

REM ============================================================================
REM Step 2: Stop Docker (optional)
REM ============================================================================

if /i "%~1"=="--docker" (
    echo.
    echo [2/2] Stopping Docker containers...
    pushd "%INFRA_DIR%"
    docker compose -f compose-infra-only.yaml down
    popd
    echo       Docker: Stopped
) else (
    echo.
    echo [2/2] Docker containers: KEPT RUNNING
    echo       Use --docker flag to stop: stop_all.bat --docker
)

echo.
echo  ============================================
echo   All services stopped.
echo  ============================================
echo.

endlocal
