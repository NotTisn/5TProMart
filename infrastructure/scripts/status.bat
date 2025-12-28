@echo off
REM ============================================================================
REM 5TPROMART - STATUS CHECK
REM ============================================================================
REM Shows the status of all services
REM ============================================================================

echo.
echo  ============================================
echo   5TPROMART - Service Status
echo  ============================================
echo.

REM Check PostgreSQL
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo   PostgreSQL   :5432   [ STOPPED ]
) else (
    echo   PostgreSQL   :5432   [ RUNNING ]
)

REM Check Keycloak
powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/health/ready' -UseBasicParsing -TimeoutSec 2;if($r.Content -match 'UP'){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo   Keycloak     :8180   [ STOPPED ]
) else (
    echo   Keycloak     :8180   [ RUNNING ]
)

REM Check Spring Boot API
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo   5TProMart    :8080   [ STOPPED ]
) else (
    echo   5TProMart    :8080   [ RUNNING ]
)

echo.
echo  ============================================
echo.
echo   Quick Commands:
echo     dev               Start everything
echo     dev --stop        Stop Java services
echo     dev --status      This status
echo     dev --clean       Nuclear reset
echo.
