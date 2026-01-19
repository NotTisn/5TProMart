@echo off
setlocal enabledelayedexpansion
REM ============================================================================
REM 5TPROMART - DEV MODE (FULL FIXED VERSION)
REM ============================================================================
REM Clone. Run. Code.
REM
REM Usage: dev [--skip-app] [--stop] [--status] [--clean]
REM ============================================================================

set "ROOT=%~dp0"
REM Xoa dau backslash o cuoi neu co de tranh loi duong dan
if "%ROOT:~-1%"=="\" set "ROOT=%ROOT:~0,-1%"
set "INFRA=%ROOT%\infrastructure"

REM ============================================================================
REM 0. LOAD .ENV VARIABLES
REM ============================================================================

echo.
if exist "%ROOT%\.env" (
    echo [Config] Loading environment variables from .env...
    REM Doc file .env, bo qua dong comment (#)
    for /f "usebackq tokens=* eol=#" %%a in ("%ROOT%\.env") do (
        set "line=%%a"
        REM Kiem tra neu dong co chua dau = thi moi xu ly
        echo "!line!" | findstr "=" >nul
        if not errorlevel 1 (
            for /f "tokens=1* delims==" %%b in ("!line!") do (
                set "key=%%b"
                set "val=%%c"
                REM Xoa khoang trang du thua (neu co)
                set "!key!=!val!"
            )
        )
    )
) else (
    echo [WARN] Khong tim thay file .env tai: %ROOT%\.env
    echo        Ung dung co the bi loi thieu API Key/Password!
    pause
    exit /b 1
)

REM ============================================================================
REM Parse Arguments
REM ============================================================================

set "AUTO_SEED=prompt"

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

if /i "%~1"=="--seed" (
    set "AUTO_SEED=yes"
)

if /i "%~1"=="--no-seed" (
    set "AUTO_SEED=no"
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
echo [0/3] Checking Docker...
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo  [ERROR] Docker is not running!
    echo.
    echo  Please start Docker Desktop and try again.
    echo.
    pause
    exit /b 1
)
echo       Docker: OK
echo.

REM --- Check for port conflicts FIRST ---
echo [1/3] Checking ports...

set "PORT_CONFLICT=0"
set "PG_RUNNING=0"
set "KC_RUNNING=0"
set "API_RUNNING=0"

REM Check PostgreSQL port
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',5432);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    REM Port is open - check if it's our container
    docker ps --format "{{.Names}}" 2>nul | findstr /i "fivetpromart-postgres" >nul 2>&1
    if errorlevel 1 (
        echo       [WARN] Port 5432 is in use by something else!
        set "PORT_CONFLICT=1"
    ) else (
        echo       PostgreSQL: Already running ^(ours^)
        set "PG_RUNNING=1"
    )
)

REM Check Keycloak port
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8180);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    docker ps --format "{{.Names}}" 2>nul | findstr /i "fivetpromart-keycloak" >nul 2>&1
    if errorlevel 1 (
        echo       [WARN] Port 8180 is in use by something else!
        set "PORT_CONFLICT=1"
    ) else (
        echo       Keycloak: Already running ^(ours^)
        set "KC_RUNNING=1"
    )
)

REM Check API port
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',8080);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    echo       API: Already running on :8080
    set "API_RUNNING=1"
)

if "%PORT_CONFLICT%"=="1" (
    echo.
    echo  [ERROR] Port conflict detected!
    echo.
    echo  Another application is using required ports.
    echo  Please stop it first, or run: dev --stop --docker
    echo.
    echo  To see what's using ports:
    echo    netstat -ano ^| findstr ":5432 :8180 :8080"
    echo.
    pause
    exit /b 1
)
echo       Ports: OK
echo.

REM ============================================================================
REM Step 2: Infrastructure
REM ============================================================================

echo [2/3] Infrastructure (PostgreSQL + Keycloak)...

if "%PG_RUNNING%"=="1" (
    if "%KC_RUNNING%"=="1" (
        echo       Already running.
        goto step3
    )
)

REM Need to start Docker services
echo       Starting Docker services...
pushd "%INFRA%"

REM --- FIX: Chi dinh file .env nam o ROOT de Docker doc duoc password ---
docker compose --env-file "%ROOT%\.env" -f compose-infra-only.yaml up -d

if errorlevel 1 (
    echo.
    echo  [ERROR] Failed to start Docker services!
    echo  Check: docker compose -f compose-infra-only.yaml logs
    echo.
    popd
    pause
    exit /b 1
)
popd

REM Wait for PostgreSQL
echo       Waiting for PostgreSQL...
call :wait_for_port 5432 30
if errorlevel 1 (
    echo  [ERROR] PostgreSQL failed to start in 30s
    echo  Check: docker logs fivetpromart-postgres
    pause
    exit /b 1
)
echo       PostgreSQL: Ready

REM Wait for Keycloak (takes longer)
echo       Waiting for Keycloak (may take 60s on first run)...
call :wait_for_keycloak 120
if errorlevel 1 (
    echo  [ERROR] Keycloak failed to start in 120s
    echo  Check: docker logs fivetpromart-keycloak
    pause
    exit /b 1
)
echo       Keycloak: Ready

REM --- Tu dong tao Admin User (Fix loi thieu admin) ---
echo       Configuring Keycloak Admin...
docker exec -e TEMP_PASS=admin fivetpromart-keycloak /opt/keycloak/bin/kc.sh bootstrap-admin user --username admin --password:env TEMP_PASS >nul 2>&1

echo       Infrastructure: OK
echo.

REM ============================================================================
REM Step 2.5: Offer Database Seeding (NEW)
REM ============================================================================

if "%AUTO_SEED%"=="prompt" (
    echo.
    echo  ╔════════════════════════════════════════════════════════════════╗
    echo  ║  Database Seed Available                                       ║
    echo  ╠════════════════════════════════════════════════════════════════╣
    echo  ║  Do you want to seed the database with test data?              ║
    echo  ║                                                                ║
    echo  ║  This includes:                                                ║
    echo  ║    • 10 Categories                                             ║
    echo  ║    • 30 Products with stock                                    ║
    echo  ║    • 15 Customers with loyalty points                          ║
    echo  ║    • 5 Suppliers                                               ║
    echo  ║    • 3 Active promotions                                       ║
    echo  ║                                                                ║
    echo  ║  Note: This is SAFE - it won't delete existing data           ║
    echo  ║        (uses ON CONFLICT DO NOTHING)                           ║
    echo  ╚════════════════════════════════════════════════════════════════╝
    echo.
    
    set /p seed_choice="  Seed database now? (y/n, default: n): "
    if /i "!seed_choice!"=="y" (
        set "AUTO_SEED=yes"
    ) else (
        set "AUTO_SEED=no"
    )
)

if "%AUTO_SEED%"=="yes" (
    echo.
    echo  [Seeding] Inserting test data...
    
    pushd "%ROOT%\infrastructure\seed"
    docker exec -i fivetpromart-postgres psql -U postgres -d fivetpromart_db < master_seed.sql >nul 2>&1
    
    if errorlevel 1 (
        echo       [WARN] Seeding failed. Tables may not exist yet.
        echo       Run 'seed-only.bat' after app starts to seed manually.
    ) else (
        echo       ✓ Database seeded successfully!
    )
    popd
    echo.
)

REM ============================================================================
REM Step 3: Verify Realm + Start App
REM ============================================================================

:step3
echo [3/3] Application...

REM --- DEBUG: Kiem tra bien Secret da load duoc chua ---
if "!KEYCLOAK_CLIENT_SECRET!"=="" (
    echo.
    echo  [CRITICAL ERROR] Khong doc duoc KEYCLOAK_CLIENT_SECRET tu file .env
    echo  Ung dung se KHONG login duoc vao Keycloak.
    echo  Vui long kiem tra file .env
    echo.
    pause
    exit /b 1
) else (
    echo       [DEBUG] Secret loaded successfully (Checking first 4 chars: !KEYCLOAK_CLIENT_SECRET:~0,4!...)
)

REM --- 1. Kiem tra Realm Keycloak (Chi canh bao, khong dung script) ---
powershell -Command "try{Invoke-WebRequest -Uri 'http://localhost:8180/realms/fivetpro' -UseBasicParsing -TimeoutSec 2 | Out-Null;exit 0}catch{exit 1}" >nul 2>&1
if errorlevel 1 (
    echo       [WARN] Realm 'fivetpro' not detected or Keycloak is starting...
)

REM --- 3. Start Spring Boot ---
echo       Starting Spring Boot...

REM Kiem tra file mvnw.cmd co ton tai khong
if not exist "mvnw.cmd" (
    echo [ERROR] Khong tim thay file mvnw.cmd! Ban dang dung sai thu muc?
    pause
    exit /b 1
)

REM --- FIX QUAN TRONG: Truyen bien vao JVM Arguments ---
REM Su dung delayed expansion !VAR! de lay gia tri chinh xac tu vong lap o tren
REM JVM Arguments (-D) an toan hon viec set bien moi truong trong CMD start
set "JAVA_CMD=mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-DKEYCLOAK_CLIENT_SECRET=!KEYCLOAK_CLIENT_SECRET!""

REM --- QUAN TRONG: Lenh nay se luon bat cua so moi ---
start "5TProMart" cmd /k "!JAVA_CMD!"

REM --- 4. Cho doi App khoi dong ---
echo       Waiting for startup...
call :wait_for_port 8080 60

if errorlevel 1 (
    echo  [WARN] App khoi dong lau hon du kien. Hay kiem tra cua so '5TProMart'.
) else (
    echo       Application: Ready
)
echo.

REM ============================================================================
REM Done!
REM ============================================================================

:done
echo  ============================================
echo   Ready!
echo  ============================================
echo.
echo   API:        http://localhost:8080
echo   Keycloak:   http://localhost:8180/admin (admin/admin)
echo   PostgreSQL: localhost:5432 (postgres/votrungtin2005)
echo.
echo   Test Users:
echo     admin / admin123           (Full access)
echo     manager / manager123       (Manager role)
echo     salesstaff / sales123      (Sales operations)
echo     warehousestaff / warehouse123 (Inventory ops)
echo.
echo   Commands:
echo     dev                      Start dev mode (prompt for seeding)
echo     dev --seed               Start and auto-seed database
echo     dev --no-seed            Start without seeding
echo     dev --stop               Stop Java services
echo     dev --stop --docker      Stop everything
echo     dev --status             Check status
echo     dev --clean              Reset all data
echo.
echo   Database Seeding:
echo     infrastructure\seed\seed-only.bat        Seed existing tables
echo     infrastructure\seed\rinse-and-seed.bat   Drop all + reseed
echo.
goto :eof

REM ============================================================================
REM FUNCTIONS
REM ============================================================================

:wait_for_port
REM Usage: call :wait_for_port <port> <timeout_seconds>
REM Returns: errorlevel 0 = success, 1 = timeout
set "wfp_port=%~1"
set "wfp_timeout=%~2"
set "wfp_elapsed=0"

:wait_port_loop
if %wfp_elapsed% geq %wfp_timeout% (
    exit /b 1
)
powershell -Command "try{(New-Object Net.Sockets.TcpClient).Connect('localhost',%wfp_port%);exit 0}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    exit /b 0
)
timeout /t 2 /nobreak >nul
set /a wfp_elapsed+=2
goto wait_port_loop

:wait_for_keycloak
REM Usage: call :wait_for_keycloak <timeout_seconds>
REM Waits for Keycloak health endpoint to return UP
set "wfk_timeout=%~1"
set "wfk_elapsed=0"

:wait_kc_loop
if %wfk_elapsed% geq %wfk_timeout% (
    exit /b 1
)
powershell -Command "try{$r=Invoke-WebRequest -Uri 'http://localhost:8180/' -UseBasicParsing -TimeoutSec 5;if($r.StatusCode -eq 200){exit 0}else{exit 1}}catch{exit 1}" >nul 2>&1
if not errorlevel 1 (
    exit /b 0
)
timeout /t 5 /nobreak >nul
set /a wfk_elapsed+=5
goto wait_kc_loop