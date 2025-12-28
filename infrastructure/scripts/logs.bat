@echo off
REM ============================================================================
REM 5TPROMART - VIEW LOGS
REM ============================================================================
REM Usage: logs.bat <service>
REM
REM Services:
REM   postgres   PostgreSQL database logs
REM   keycloak   Keycloak identity provider logs
REM ============================================================================

set "SCRIPT_DIR=%~dp0"
set "INFRA_DIR=%SCRIPT_DIR%.."

if "%~1"=="" (
    echo.
    echo  Usage: logs.bat ^<service^>
    echo.
    echo  Services:
    echo    postgres   PostgreSQL logs
    echo    keycloak   Keycloak logs
    echo.
    exit /b 1
)

set "SERVICE=%~1"

pushd "%INFRA_DIR%"

if /i "%SERVICE%"=="postgres" (
    docker compose -f compose-infra-only.yaml logs -f postgres
    goto done
)

if /i "%SERVICE%"=="keycloak" (
    docker compose -f compose-infra-only.yaml logs -f keycloak
    goto done
)

echo.
echo  [ERROR] Unknown service: %SERVICE%
echo  Available: postgres, keycloak
echo.
exit /b 1

:done
popd
