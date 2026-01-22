@echo off
REM ============================================================================
REM 5TPROMART - DEV MODE v2.0
REM ============================================================================
REM The Intelligent Dev Environment - One Command to Rule Them All
REM
REM Usage:
REM   dev              Start all services (PostgreSQL, Keycloak, API, AI, FE)
REM   dev --watch      Start all + open error watcher window
REM   dev --status     Rich status view with health checks
REM   dev --logs       Open logs folder
REM   dev --tail       Tail all service logs
REM   dev --kill       Stop all services
REM   dev --clean      Nuclear reset (kill + delete targets + docker down)
REM   dev --infra      Start infrastructure only (Docker)
REM   dev --no-ai      Start without AI service
REM   dev --no-fe      Start without Frontend
REM   dev --seed       Auto-seed database
REM
REM Managed Services:
REM   PostgreSQL  :5432   (Docker)
REM   Keycloak    :8180   (Docker)
REM   Spring Boot :8080   (Java)
REM   AI Service  :8090   (Python/FastAPI)
REM   Frontend    :5173   (Vite)
REM ============================================================================

set "ROOT=%~dp0"
if "%ROOT:~-1%"=="\" set "ROOT=%ROOT:~0,-1%"
set "SCRIPT=%ROOT%\infrastructure\scripts\dev-mode.ps1"

if not exist "%SCRIPT%" (
    echo [ERROR] PowerShell engine not found: %SCRIPT%
    echo         Run from the 5TProMart_be directory.
    pause
    exit /b 1
)

set "PS_ARGS="

:parse_args
if "%~1"=="" goto run_ps
if /I "%~1"=="--watch"    set "PS_ARGS=%PS_ARGS% -Watch"     & shift & goto parse_args
if /I "%~1"=="-watch"     set "PS_ARGS=%PS_ARGS% -Watch"     & shift & goto parse_args
if /I "%~1"=="--status"   set "PS_ARGS=%PS_ARGS% -Status"    & shift & goto parse_args
if /I "%~1"=="-status"    set "PS_ARGS=%PS_ARGS% -Status"    & shift & goto parse_args
if /I "%~1"=="--logs"     set "PS_ARGS=%PS_ARGS% -Logs"      & shift & goto parse_args
if /I "%~1"=="-logs"      set "PS_ARGS=%PS_ARGS% -Logs"      & shift & goto parse_args
if /I "%~1"=="--tail"     set "PS_ARGS=%PS_ARGS% -Tail"      & shift & goto parse_args
if /I "%~1"=="-tail"      set "PS_ARGS=%PS_ARGS% -Tail"      & shift & goto parse_args
if /I "%~1"=="--kill"     set "PS_ARGS=%PS_ARGS% -Kill"      & shift & goto parse_args
if /I "%~1"=="-kill"      set "PS_ARGS=%PS_ARGS% -Kill"      & shift & goto parse_args
if /I "%~1"=="--stop"     set "PS_ARGS=%PS_ARGS% -Kill"      & shift & goto parse_args
if /I "%~1"=="-stop"      set "PS_ARGS=%PS_ARGS% -Kill"      & shift & goto parse_args
if /I "%~1"=="--clean"    set "PS_ARGS=%PS_ARGS% -Clean"     & shift & goto parse_args
if /I "%~1"=="-clean"     set "PS_ARGS=%PS_ARGS% -Clean"     & shift & goto parse_args
if /I "%~1"=="--infra"    set "PS_ARGS=%PS_ARGS% -Infra"     & shift & goto parse_args
if /I "%~1"=="-infra"     set "PS_ARGS=%PS_ARGS% -Infra"     & shift & goto parse_args
if /I "%~1"=="--no-ai"    set "PS_ARGS=%PS_ARGS% -NoAI"      & shift & goto parse_args
if /I "%~1"=="-no-ai"     set "PS_ARGS=%PS_ARGS% -NoAI"      & shift & goto parse_args
if /I "%~1"=="--no-fe"    set "PS_ARGS=%PS_ARGS% -NoFE"      & shift & goto parse_args
if /I "%~1"=="-no-fe"     set "PS_ARGS=%PS_ARGS% -NoFE"      & shift & goto parse_args
if /I "%~1"=="--seed"     set "PS_ARGS=%PS_ARGS% -Seed"      & shift & goto parse_args
if /I "%~1"=="-seed"      set "PS_ARGS=%PS_ARGS% -Seed"      & shift & goto parse_args
if /I "%~1"=="--skip-app" set "PS_ARGS=%PS_ARGS% -Infra"     & shift & goto parse_args
shift
goto parse_args

:run_ps
powershell -ExecutionPolicy Bypass -File "%SCRIPT%" %PS_ARGS%
