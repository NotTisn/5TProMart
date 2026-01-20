@echo off
REM ============================================================================
REM 5TPROMART - STATUS CHECK v2.0
REM ============================================================================
REM Delegates to dev-mode.ps1 for rich status output
REM ============================================================================

set "ROOT=%~dp0..\..\"
if "%ROOT:~-1%"=="\" set "ROOT=%ROOT:~0,-1%"
set "SCRIPT=%ROOT%\infrastructure\scripts\dev-mode.ps1"

if exist "%SCRIPT%" (
    powershell -ExecutionPolicy Bypass -File "%SCRIPT%" -Status
) else (
    echo [ERROR] dev-mode.ps1 not found
    echo Run this from the 5TProMart_be\infrastructure\scripts directory
)
