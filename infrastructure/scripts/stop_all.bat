@echo off
REM ============================================================================
REM 5TPROMART - STOP ALL SERVICES v2.0
REM ============================================================================
REM Delegates to dev-mode.ps1 for consistent service management
REM ============================================================================

set "SCRIPT=%~dp0dev-mode.ps1"

if exist "%SCRIPT%" (
    powershell -ExecutionPolicy Bypass -File "%SCRIPT%" -Kill
) else (
    echo [ERROR] dev-mode.ps1 not found
)

if /i "%~1"=="--docker" (
    echo.
    echo Stopping Docker containers...
    pushd "%~dp0.."
    docker compose -f compose-infra-only.yaml down
    popd
)
