# ============================================================================
# 5TPROMART Dev Mode v2.0 - The Intelligent Dev Environment
# ============================================================================
# 
# Usage:
#   .\dev-mode.ps1              Start all services
#   .\dev-mode.ps1 -Watch       Start all + error watcher
#   .\dev-mode.ps1 -Status      Rich status with health checks
#   .\dev-mode.ps1 -Logs        Open logs folder
#   .\dev-mode.ps1 -Tail        Tail all logs in real-time
#   .\dev-mode.ps1 -Kill        Kill all managed services
#   .\dev-mode.ps1 -Clean       Nuclear reset (kill + clean + docker down)
#   .\dev-mode.ps1 -Infra       Start Docker infrastructure only
#   .\dev-mode.ps1 -NoAI        Start without AI service
#   .\dev-mode.ps1 -NoFE        Start without Frontend
#   .\dev-mode.ps1 -Seed        Auto-seed database
#
# ============================================================================

param(
    [switch]$Watch,
    [switch]$Status,
    [switch]$Logs,
    [switch]$Tail,
    [switch]$Kill,
    [switch]$Clean,
    [switch]$Infra,
    [switch]$NoAI,
    [switch]$NoFE,
    [switch]$Seed
)

$ErrorActionPreference = "Continue"

# ============================================================================
# PATH CONFIGURATION
# ============================================================================

$SCRIPT_DIR = Split-Path -Parent $MyInvocation.MyCommand.Path
$INFRA_DIR = Split-Path -Parent $SCRIPT_DIR
$BE_DIR = Split-Path -Parent $INFRA_DIR
$ROOT_DIR = Split-Path -Parent $BE_DIR
$LOG_DIR = Join-Path $INFRA_DIR "logs"
$AI_DIR = Join-Path $ROOT_DIR "promart-ai-service"
$FE_DIR = Join-Path $ROOT_DIR "FiveTProMart_fe"

$PROJECT_NAME = "5TProMart"

# ============================================================================
# SERVICE DEFINITIONS
# ============================================================================

$INFRA_PORTS = @{
    PostgreSQL = 5432
    Keycloak   = 8180
}

$SERVICES = @{
    SpringBoot = @{ Name = "api";       Port = 8080; Type = "java";   Dir = $BE_DIR }
    AIService  = @{ Name = "ai";        Port = 8090; Type = "python"; Dir = $AI_DIR }
    Frontend   = @{ Name = "frontend";  Port = 5173; Type = "node";   Dir = $FE_DIR }
}

# ============================================================================
# UTILITY FUNCTIONS
# ============================================================================

function Test-Port {
    param([int]$Port)
    # Use Get-NetTCPConnection which is more reliable than TcpClient
    # TcpClient.Connect() can be refused by some servers (like Vite) even when port is listening
    try {
        $conn = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
        return ($null -ne $conn)
    } catch {
        return $false
    }
}

function Test-HttpHealth {
    param([string]$Url, [int]$TimeoutSec = 3)
    try {
        $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec $TimeoutSec -ErrorAction Stop
        return $response.StatusCode -eq 200
    } catch {
        return $false
    }
}

function Write-Status {
    param([string]$Message, [string]$Type = "INFO")
    $ts = Get-Date -Format "HH:mm:ss"
    switch ($Type) {
        "OK"      { Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray; Write-Host "[OK] " -NoNewline -ForegroundColor Green; Write-Host $Message }
        "WAIT"    { Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray; Write-Host "[..] " -NoNewline -ForegroundColor Yellow; Write-Host $Message }
        "FAIL"    { Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray; Write-Host "[XX] " -NoNewline -ForegroundColor Red; Write-Host $Message }
        "INFO"    { Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray; Write-Host "[--] " -NoNewline -ForegroundColor Cyan; Write-Host $Message }
        "SKIP"    { Write-Host "[$ts] " -NoNewline -ForegroundColor DarkGray; Write-Host "[--] " -NoNewline -ForegroundColor DarkGray; Write-Host $Message }
        default   { Write-Host "[$ts] $Message" }
    }
}

function Get-ProcessOnPort {
    param([int]$Port)
    try {
        $conn = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($conn) {
            $proc = Get-Process -Id $conn.OwningProcess -ErrorAction SilentlyContinue
            return $proc
        }
    } catch { }
    return $null
}

function Stop-ProcessOnPort {
    param([int]$Port, [string]$Name = "")
    $proc = Get-ProcessOnPort -Port $Port
    if ($proc) {
        Write-Status "Stopping $Name on :$Port (PID: $($proc.Id))..." "WAIT"
        Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
        Start-Sleep -Milliseconds 500
        return $true
    }
    return $false
}

function Show-Banner {
    Clear-Host
    Write-Host ""
    Write-Host "  ================================================================" -ForegroundColor Cyan
    Write-Host "       $PROJECT_NAME Dev Mode v2.0 - Intelligent Dev Environment " -ForegroundColor Cyan
    Write-Host "  ================================================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "  Backend:   $BE_DIR" -ForegroundColor DarkGray
    Write-Host "  Frontend:  $FE_DIR" -ForegroundColor DarkGray
    Write-Host "  AI:        $AI_DIR" -ForegroundColor DarkGray
    Write-Host "  Logs:      $LOG_DIR" -ForegroundColor DarkGray
    Write-Host ""
}

# ============================================================================
# STATUS COMMAND - Rich status with health checks
# ============================================================================

function Show-ServiceStatus {
    Show-Banner
    Write-Host "  SERVICE STATUS" -ForegroundColor White
    Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray
    
    # Infrastructure
    Write-Host ""
    Write-Host "  Infrastructure (Docker):" -ForegroundColor Gray
    
    # PostgreSQL
    $pgUp = Test-Port 5432
    $pgStatus = if ($pgUp) { "[UP]  " } else { "[DOWN]" }
    $pgColor = if ($pgUp) { "Green" } else { "Red" }
    $pgHealth = ""
    if ($pgUp) {
        try {
            $result = docker exec fivetpromart-postgres pg_isready -U postgres 2>$null
            if ($LASTEXITCODE -eq 0) { $pgHealth = "(healthy)" }
        } catch { }
    }
    Write-Host "    $pgStatus " -NoNewline -ForegroundColor $pgColor
    Write-Host "PostgreSQL   :5432  " -NoNewline -ForegroundColor White
    Write-Host $pgHealth -ForegroundColor Green
    
    # Keycloak
    $kcUp = Test-Port 8180
    $kcStatus = if ($kcUp) { "[UP]  " } else { "[DOWN]" }
    $kcColor = if ($kcUp) { "Green" } else { "Red" }
    $kcHealth = ""
    if ($kcUp) {
        if (Test-HttpHealth "http://localhost:8180/health/ready") {
            $kcHealth = "(healthy)"
        } else {
            $kcHealth = "(starting...)"
            $kcColor = "Yellow"
        }
    }
    Write-Host "    $kcStatus " -NoNewline -ForegroundColor $kcColor
    Write-Host "Keycloak     :8180  " -NoNewline -ForegroundColor White
    Write-Host $kcHealth -ForegroundColor Green
    
    # Application Services
    Write-Host ""
    Write-Host "  Application Services:" -ForegroundColor Gray
    
    # Spring Boot API
    $apiUp = Test-Port 8080
    $apiStatus = if ($apiUp) { "[UP]  " } else { "[DOWN]" }
    $apiColor = if ($apiUp) { "Green" } else { "Red" }
    $apiHealth = ""
    $apiProc = Get-ProcessOnPort 8080
    if ($apiUp -and $apiProc) {
        $apiHealth = "(PID: $($apiProc.Id))"
        if (Test-HttpHealth "http://localhost:8080/actuator/health") {
            $apiHealth = "(healthy, PID: $($apiProc.Id))"
        }
    }
    Write-Host "    $apiStatus " -NoNewline -ForegroundColor $apiColor
    Write-Host "Spring Boot  :8080  " -NoNewline -ForegroundColor White
    Write-Host $apiHealth -ForegroundColor Gray
    
    # AI Service
    $aiUp = Test-Port 8090
    $aiStatus = if ($aiUp) { "[UP]  " } else { "[DOWN]" }
    $aiColor = if ($aiUp) { "Green" } else { "Red" }
    $aiHealth = ""
    $aiProc = Get-ProcessOnPort 8090
    if ($aiUp -and $aiProc) {
        $aiHealth = "(PID: $($aiProc.Id))"
        if (Test-HttpHealth "http://localhost:8090/health") {
            $aiHealth = "(healthy, PID: $($aiProc.Id))"
        }
    }
    Write-Host "    $aiStatus " -NoNewline -ForegroundColor $aiColor
    Write-Host "AI Service   :8090  " -NoNewline -ForegroundColor White
    Write-Host $aiHealth -ForegroundColor Gray
    
    # Frontend
    $feUp = Test-Port 5173
    $feStatus = if ($feUp) { "[UP]  " } else { "[DOWN]" }
    $feColor = if ($feUp) { "Green" } else { "Red" }
    $feHealth = ""
    $feProc = Get-ProcessOnPort 5173
    if ($feUp -and $feProc) {
        $feHealth = "(PID: $($feProc.Id))"
    }
    Write-Host "    $feStatus " -NoNewline -ForegroundColor $feColor
    Write-Host "Frontend     :5173  " -NoNewline -ForegroundColor White
    Write-Host $feHealth -ForegroundColor Gray
    
    # Recent logs summary
    Write-Host ""
    Write-Host "  Recent Activity:" -ForegroundColor Gray
    if (Test-Path $LOG_DIR) {
        $recentLogs = Get-ChildItem $LOG_DIR -Filter "*.log" -ErrorAction SilentlyContinue | 
                      Sort-Object LastWriteTime -Descending | 
                      Select-Object -First 3
        if ($recentLogs) {
            foreach ($log in $recentLogs) {
                $age = [int]((Get-Date) - $log.LastWriteTime).TotalMinutes
                $ageStr = if ($age -lt 1) { "just now" } elseif ($age -lt 60) { "$age min ago" } else { "$([int]($age/60))h ago" }
                Write-Host "    $($log.Name.PadRight(40)) $ageStr" -ForegroundColor DarkGray
            }
        } else {
            Write-Host "    No logs yet" -ForegroundColor DarkGray
        }
    } else {
        Write-Host "    Logs folder not created yet" -ForegroundColor DarkGray
    }
    
    Write-Host ""
    Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray
    Write-Host "  Commands:" -ForegroundColor White
    Write-Host "    dev              Start all services" -ForegroundColor DarkGray
    Write-Host "    dev --watch      Start all + error watcher" -ForegroundColor DarkGray
    Write-Host "    dev --logs       Open logs folder" -ForegroundColor DarkGray
    Write-Host "    dev --tail       Tail all logs live" -ForegroundColor DarkGray
    Write-Host "    dev --kill       Stop all services" -ForegroundColor DarkGray
    Write-Host "    dev --clean      Nuclear reset" -ForegroundColor DarkGray
    Write-Host ""
}

# ============================================================================
# KILL COMMAND - Stop all managed services
# ============================================================================

function Stop-AllServices {
    Write-Status "Stopping all managed services..." "WAIT"
    
    # Frontend (node)
    $null = Stop-ProcessOnPort -Port 5173 -Name "Frontend"
    
    # AI Service (python)
    $null = Stop-ProcessOnPort -Port 8090 -Name "AI Service"
    
    # Kill any orphan Python processes from uvicorn
    Get-Process python -ErrorAction SilentlyContinue | 
        Where-Object { $_.CommandLine -like "*uvicorn*" -or $_.CommandLine -like "*8090*" } |
        ForEach-Object { 
            Write-Status "Killing orphan Python process (PID: $($_.Id))" "WAIT"
            Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue 
        }
    
    # Spring Boot (java)
    $null = Stop-ProcessOnPort -Port 8080 -Name "Spring Boot"
    
    # Also kill any orphan Java processes from this project
    Get-Process java -ErrorAction SilentlyContinue | 
        Where-Object { $_.Path -like "*5TProMart*" -or $_.CommandLine -like "*5TProMart*" } |
        ForEach-Object { 
            Write-Status "Killing orphan Java process (PID: $($_.Id))" "WAIT"
            Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue 
        }
    
    # Kill any orphan node processes from vite
    Get-Process node -ErrorAction SilentlyContinue | 
        Where-Object { $_.CommandLine -like "*vite*" -or $_.CommandLine -like "*5173*" } |
        ForEach-Object { 
            Write-Status "Killing orphan Node process (PID: $($_.Id))" "WAIT"
            Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue 
        }
    
    # Clean up runner scripts
    $scriptsPath = Join-Path $INFRA_DIR "scripts"
    @(".run-api.ps1", ".run-ai.ps1", ".run-fe.ps1") | ForEach-Object {
        $f = Join-Path $scriptsPath $_
        if (Test-Path $f) { Remove-Item $f -Force -ErrorAction SilentlyContinue }
    }
        Write-Status "All services stopped" "OK"
}

# ============================================================================
# CLEAN COMMAND - Nuclear reset
# ============================================================================

function Invoke-CleanReset {
    Show-Banner
    Write-Host "  NUCLEAR RESET" -ForegroundColor Red
    Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray
    
    # Stop all services
    Stop-AllServices
    
    # Clean Maven target
    Write-Status "Cleaning Maven target..." "WAIT"
    Push-Location $BE_DIR
    if (Test-Path "target") {
        Remove-Item "target" -Recurse -Force -ErrorAction SilentlyContinue
    }
    Pop-Location
    Write-Status "Maven target cleaned" "OK"
    
    # Clean logs
    Write-Status "Cleaning logs..." "WAIT"
    if (Test-Path $LOG_DIR) {
        Remove-Item "$LOG_DIR\*" -Force -ErrorAction SilentlyContinue
    }
    Write-Status "Logs cleaned" "OK"
    
    # Stop Docker
    Write-Status "Stopping Docker infrastructure..." "WAIT"
    Push-Location $INFRA_DIR
    docker compose -f compose-infra-only.yaml down 2>$null
    Pop-Location
    Write-Status "Docker stopped" "OK"
    
    Write-Host ""
    Write-Status "Nuclear reset complete. Run 'dev' to start fresh." "OK"
}

# ============================================================================
# LOGS COMMANDS
# ============================================================================

function Open-LogsFolder {
    if (-not (Test-Path $LOG_DIR)) {
        New-Item -ItemType Directory -Path $LOG_DIR -Force | Out-Null
    }
    Start-Process explorer $LOG_DIR
    Write-Status "Opened logs folder: $LOG_DIR" "OK"
}

function Start-LogTail {
    if (-not (Test-Path $LOG_DIR)) {
        Write-Status "No logs folder yet. Start services first." "FAIL"
        return
    }
    
    $runnerScript = Join-Path $INFRA_DIR "scripts\.run-tail.ps1"
    
    $scriptContent = @"
# Auto-generated Log Tail
`$host.UI.RawUI.WindowTitle = 'Log Tail'
`$LogDir = '$LOG_DIR'

Write-Host ''
Write-Host '  ============================================' -ForegroundColor Cyan
Write-Host '   LOG TAIL - All Services' -ForegroundColor Cyan
Write-Host '  ============================================' -ForegroundColor Cyan
Write-Host ''
Write-Host "  Watching: `$LogDir" -ForegroundColor DarkGray
Write-Host '  Press Ctrl+C to stop' -ForegroundColor DarkGray
Write-Host ''

`$filePositions = @{}

while (`$true) {
    `$files = Get-ChildItem -Path `$LogDir -Filter '*.log' -ErrorAction SilentlyContinue
    
    foreach (`$file in `$files) {
        `$path = `$file.FullName
        `$svcName = if (`$file.Name -match '^(\w+)-') { `$matches[1] } else { 'unknown' }
        
        if (-not `$filePositions.ContainsKey(`$path)) {
            `$filePositions[`$path] = (Get-Content `$path -ErrorAction SilentlyContinue).Count
        }
        
        try {
            `$content = Get-Content `$path -ErrorAction SilentlyContinue
            `$lineCount = `$content.Count
            
            if (`$lineCount -gt `$filePositions[`$path]) {
                `$newLines = `$content[`$filePositions[`$path]..(`$lineCount - 1)]
                `$filePositions[`$path] = `$lineCount
                
                foreach (`$line in `$newLines) {
                    `$color = 'Gray'
                    if (`$line -match 'ERROR|Exception|FATAL') { `$color = 'Red' }
                    elseif (`$line -match 'WARN') { `$color = 'Yellow' }
                    elseif (`$line -match 'Started|Ready|Listening') { `$color = 'Green' }
                    
                    `$ts = Get-Date -Format 'HH:mm:ss'
                    Write-Host "[`$ts] " -NoNewline -ForegroundColor DarkGray
                    Write-Host "`$(`$svcName.ToUpper().PadRight(10)) " -NoNewline -ForegroundColor Cyan
                    Write-Host `$line -ForegroundColor `$color
                }
            }
        } catch { }
    }
    
    Start-Sleep -Milliseconds 200
}
"@
    
    # Write script with UTF-8 no BOM
    [IO.File]::WriteAllText($runnerScript, $scriptContent, [Text.UTF8Encoding]::new($false))
    
    # Use cmd.exe start command for reliable window title
    $startCmd = "start `"Log Tail - 5TProMart`" powershell -NoExit -ExecutionPolicy Bypass -File `"$runnerScript`""
    Start-Process cmd.exe -ArgumentList "/c", $startCmd
    Write-Status "Log tail started in new window" "OK"
}

# ============================================================================
# INFRASTRUCTURE
# ============================================================================

function Start-Infrastructure {
    Write-Status "Starting Docker infrastructure..." "WAIT"
    
    Push-Location $INFRA_DIR
    docker compose -f compose-infra-only.yaml up -d 2>&1 | Out-Null
    Pop-Location
    
    # Wait for PostgreSQL
    Write-Status "Waiting for PostgreSQL..." "WAIT"
    for ($i = 0; $i -lt 30; $i++) {
        if (Test-Port 5432) { break }
        Start-Sleep -Seconds 1
    }
    
    if (Test-Port 5432) {
        Write-Status "PostgreSQL ready" "OK"
    } else {
        Write-Status "PostgreSQL timeout" "FAIL"
    }
    
    # Wait for Keycloak
    Write-Status "Waiting for Keycloak..." "WAIT"
    for ($i = 0; $i -lt 60; $i++) {
        if (Test-HttpHealth "http://localhost:8180/health/ready") { break }
        Start-Sleep -Seconds 1
    }
    
    if (Test-HttpHealth "http://localhost:8180/health/ready") {
        Write-Status "Keycloak ready" "OK"
    } else {
        Write-Status "Keycloak still starting (may need more time)" "WAIT"
    }
}

# ============================================================================
# SERVICE LAUNCHERS
# Using cmd.exe with /k for reliable window titles and visibility
# ============================================================================

function Start-SpringBoot {
    if (Test-Port 8080) {
        Write-Status "Spring Boot already running on :8080" "OK"
        return
    }
    
    if (-not (Test-Path $LOG_DIR)) { New-Item -ItemType Directory -Path $LOG_DIR -Force | Out-Null }
    
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    $logFile = Join-Path $LOG_DIR "api-$timestamp.log"
    $runnerScript = Join-Path $INFRA_DIR "scripts\.run-api.ps1"
    
    # Create the runner script
    $scriptContent = @"
# Auto-generated API runner
`$ErrorActionPreference = 'Continue'
`$host.UI.RawUI.WindowTitle = '[API] Starting... (:8080)'

`$logFile = '$logFile'
`$beDir = '$BE_DIR'

Write-Host '' 
Write-Host '  ============================================' -ForegroundColor Cyan
Write-Host '   [API] Spring Boot - Port 8080' -ForegroundColor Cyan
Write-Host '  ============================================' -ForegroundColor Cyan
Write-Host ''
Write-Host "  Log: `$logFile" -ForegroundColor DarkGray
Write-Host ''

Set-Location `$beDir

# Load .env if exists
if (Test-Path '.env') {
    Get-Content '.env' | ForEach-Object {
        if (`$_ -match '^([^#][^=]+)=(.*)$') {
            [Environment]::SetEnvironmentVariable(`$matches[1].Trim(), `$matches[2].Trim(), 'Process')
        }
    }
}

`$script:errorCount = 0

& .\mvnw.cmd spring-boot:run -DskipTests 2>&1 | ForEach-Object {
    `$line = `$_.ToString()
    
    # Always log to file
    Add-Content -Path `$logFile -Value `$line -ErrorAction SilentlyContinue
    
    # Show errors
    if (`$line -match 'ERROR|Exception|FATAL|BUILD FAILURE|COMPILATION ERROR') {
        `$script:errorCount++
        Write-Host `$line -ForegroundColor Red
        `$host.UI.RawUI.WindowTitle = '[API] ERROR! (:8080)'
    }
    # Show startup success
    elseif (`$line -match 'Started .+ in .+ seconds|Tomcat started') {
        Write-Host `$line -ForegroundColor Green
        `$host.UI.RawUI.WindowTitle = '[API] OK (:8080)'
    }
    # Show compilation progress
    elseif (`$line -match 'Compiling|Building') {
        Write-Host `$line -ForegroundColor Yellow
    }
}

Write-Host ''
if (`$script:errorCount -gt 0) {
    Write-Host '=== CRASHED ===' -ForegroundColor Red
    Write-Host 'Last 30 lines:' -ForegroundColor DarkGray
    Get-Content `$logFile -Tail 30 | ForEach-Object { Write-Host `$_ -ForegroundColor DarkGray }
}
Read-Host 'Press Enter to close'
"@
    
    # Write script with UTF-8 no BOM
    [IO.File]::WriteAllText($runnerScript, $scriptContent, [Text.UTF8Encoding]::new($false))
    
    # Use cmd.exe start command for reliable window title
    # Note: start command requires title in quotes as first arg
    $startCmd = "start `"API - 5TProMart`" powershell -NoExit -ExecutionPolicy Bypass -File `"$runnerScript`""
    Start-Process cmd.exe -ArgumentList "/c", $startCmd
    
    Write-Status "Spring Boot starting (log: api-$timestamp.log)" "WAIT"
}

function Start-AIService {
    if (Test-Port 8090) {
        Write-Status "AI Service already running on :8090" "OK"
        return
    }
    
    if (-not (Test-Path $AI_DIR)) {
        Write-Status "AI Service directory not found: $AI_DIR" "FAIL"
        return
    }
    
    if (-not (Test-Path $LOG_DIR)) { New-Item -ItemType Directory -Path $LOG_DIR -Force | Out-Null }
    
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    $logFile = Join-Path $LOG_DIR "ai-$timestamp.log"
    $runnerScript = Join-Path $INFRA_DIR "scripts\.run-ai.ps1"
    
    # Create the runner script
    $scriptContent = @"
# Auto-generated AI Service runner
`$ErrorActionPreference = 'Continue'
`$host.UI.RawUI.WindowTitle = '[AI] Starting... (:8090)'

`$logFile = '$logFile'
`$aiDir = '$AI_DIR'

Write-Host '' 
Write-Host '  ============================================' -ForegroundColor Magenta
Write-Host '   [AI] AI Service - Port 8090' -ForegroundColor Magenta
Write-Host '  ============================================' -ForegroundColor Magenta
Write-Host ''
Write-Host "  Log: `$logFile" -ForegroundColor DarkGray
Write-Host ''

Set-Location `$aiDir

# Setup venv if needed
if (-not (Test-Path '.venv')) {
    Write-Host 'Creating virtual environment...' -ForegroundColor Yellow
    python -m venv .venv
}

# Activate and install deps
Write-Host 'Activating venv and checking deps...' -ForegroundColor Yellow
& .\.venv\Scripts\Activate.ps1
pip install -r requirements.txt --quiet 2>`$null

# Copy .env if needed
if (-not (Test-Path '.env') -and (Test-Path '.env.example')) {
    Copy-Item '.env.example' '.env'
}

Write-Host 'Starting uvicorn...' -ForegroundColor Yellow
& .\.venv\Scripts\python.exe -m uvicorn src.app:app --host 0.0.0.0 --port 8090 --reload 2>&1 | ForEach-Object {
    `$line = `$_.ToString()
    Add-Content -Path `$logFile -Value `$line -ErrorAction SilentlyContinue
    
    if (`$line -match 'ERROR|Exception|FATAL') {
        Write-Host `$line -ForegroundColor Red
        `$host.UI.RawUI.WindowTitle = '[AI] ERROR! (:8090)'
    }
    elseif (`$line -match 'Uvicorn running|Application startup complete') {
        Write-Host `$line -ForegroundColor Green
        `$host.UI.RawUI.WindowTitle = '[AI] OK (:8090)'
    }
    elseif (`$line -match 'WARNING') {
        Write-Host `$line -ForegroundColor Yellow
    }
}

Read-Host 'Press Enter to close'
"@
    
    # Write script with UTF-8 no BOM
    [IO.File]::WriteAllText($runnerScript, $scriptContent, [Text.UTF8Encoding]::new($false))
    
    # Use cmd.exe start command for reliable window title
    # Use cmd.exe start command for reliable window title
    $startCmd = "start `"AI - 5TProMart`" powershell -NoExit -ExecutionPolicy Bypass -File `"$runnerScript`""
    Start-Process cmd.exe -ArgumentList "/c", $startCmd
    
    Write-Status "AI Service starting (log: ai-$timestamp.log)" "WAIT"
}

function Start-Frontend {
    if (Test-Port 5173) {
        Write-Status "Frontend already running on :5173" "OK"
        return
    }
    
    if (-not (Test-Path $FE_DIR)) {
        Write-Status "Frontend directory not found: $FE_DIR" "FAIL"
        return
    }
    
    if (-not (Test-Path $LOG_DIR)) { New-Item -ItemType Directory -Path $LOG_DIR -Force | Out-Null }
    
    $timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
    $logFile = Join-Path $LOG_DIR "frontend-$timestamp.log"
    $runnerScript = Join-Path $INFRA_DIR "scripts\.run-fe.ps1"
    
    # Create the runner script
    $scriptContent = @"
# Auto-generated Frontend runner
`$ErrorActionPreference = 'Continue'
`$host.UI.RawUI.WindowTitle = '[FE] Starting... (:5173)'

`$logFile = '$logFile'
`$feDir = '$FE_DIR'

Write-Host '' 
Write-Host '  ============================================' -ForegroundColor Green
Write-Host '   [FE] Frontend - Port 5173' -ForegroundColor Green
Write-Host '  ============================================' -ForegroundColor Green
Write-Host ''
Write-Host "  Log: `$logFile" -ForegroundColor DarkGray
Write-Host ''

Set-Location `$feDir

& npm run dev 2>&1 | ForEach-Object {
    `$line = `$_.ToString()
    Add-Content -Path `$logFile -Value `$line -ErrorAction SilentlyContinue
    
    if (`$line -match 'ERROR|error|failed') {
        Write-Host `$line -ForegroundColor Red
        `$host.UI.RawUI.WindowTitle = '[FE] ERROR! (:5173)'
    }
    elseif (`$line -match 'ready in|VITE.*ready|Local:') {
        Write-Host `$line -ForegroundColor Green
        `$host.UI.RawUI.WindowTitle = '[FE] OK (:5173)'
    }
    elseif (`$line -match 'warning|WARN') {
        Write-Host `$line -ForegroundColor Yellow
    }
}

Read-Host 'Press Enter to close'
"@
    
    # Write script with UTF-8 no BOM
    [IO.File]::WriteAllText($runnerScript, $scriptContent, [Text.UTF8Encoding]::new($false))
    
    # Use cmd.exe start command for reliable window title
    $startCmd = "start `"Frontend - 5TProMart`" powershell -NoExit -ExecutionPolicy Bypass -File `"$runnerScript`""
    Start-Process cmd.exe -ArgumentList "/c", $startCmd
    
    Write-Status "Frontend starting (log: frontend-$timestamp.log)" "WAIT"
}

# ============================================================================
# ERROR WATCHER
# ============================================================================

function Start-ErrorWatcher {
    if (-not (Test-Path $LOG_DIR)) {
        New-Item -ItemType Directory -Path $LOG_DIR -Force | Out-Null
    }
    
    $runnerScript = Join-Path $INFRA_DIR "scripts\.run-watcher.ps1"
    
    $scriptContent = @"
# Auto-generated Error Watcher
`$host.UI.RawUI.WindowTitle = 'Error Watcher'
`$LogDir = '$LOG_DIR'

Write-Host ''
Write-Host '  ============================================' -ForegroundColor Red
Write-Host '   ERROR WATCHER' -ForegroundColor Red
Write-Host '  ============================================' -ForegroundColor Red
Write-Host ''
Write-Host "  Watching: `$LogDir" -ForegroundColor DarkGray
Write-Host '  Shows: ERROR, Exception, BUILD FAILURE only' -ForegroundColor DarkGray
Write-Host ''

`$filePositions = @{}
`$serviceColors = @{
    'api' = 'Cyan'
    'ai' = 'Magenta'
    'frontend' = 'Green'
}

while (`$true) {
    `$files = Get-ChildItem -Path `$LogDir -Filter '*.log' -ErrorAction SilentlyContinue
    
    foreach (`$file in `$files) {
        `$path = `$file.FullName
        `$svcName = if (`$file.Name -match '^(\w+)-') { `$matches[1] } else { 'unknown' }
        `$color = if (`$serviceColors.ContainsKey(`$svcName)) { `$serviceColors[`$svcName] } else { 'Gray' }
        
        if (-not `$filePositions.ContainsKey(`$path)) {
            `$filePositions[`$path] = 0
        }
        
        try {
            `$content = Get-Content `$path -ErrorAction SilentlyContinue
            `$lineCount = `$content.Count
            
            if (`$lineCount -gt `$filePositions[`$path]) {
                `$newLines = `$content[`$filePositions[`$path]..(`$lineCount - 1)]
                `$filePositions[`$path] = `$lineCount
                
                foreach (`$line in `$newLines) {
                    `$isError = `$line -match 'ERROR|Exception|FATAL|BUILD FAILURE|COMPILATION ERROR'
                    `$isNoise = `$line -match 'ExceptionHandler|ErrorController|SLF4J'
                    
                    if (`$isError -and -not `$isNoise) {
                        `$ts = Get-Date -Format 'HH:mm:ss'
                        Write-Host "[`$ts] " -NoNewline -ForegroundColor DarkGray
                        Write-Host "`$(`$svcName.ToUpper().PadRight(10)) " -NoNewline -ForegroundColor `$color
                        Write-Host `$line -ForegroundColor Red
                    }
                }
            }
        } catch { }
    }
    
    Start-Sleep -Milliseconds 300
}
"@
    
    # Write script with UTF-8 no BOM
    [IO.File]::WriteAllText($runnerScript, $scriptContent, [Text.UTF8Encoding]::new($false))
    
    # Use cmd.exe start command for reliable window title
    $startCmd = "start `"Error Watcher - 5TProMart`" powershell -NoExit -ExecutionPolicy Bypass -File `"$runnerScript`""
    Start-Process cmd.exe -ArgumentList "/c", $startCmd
    Write-Status "Error watcher started" "OK"
}

# ============================================================================
# DATABASE SEEDING
# ============================================================================

function Invoke-DatabaseSeed {
    Write-Status "Seeding database..." "WAIT"
    
    $seedScript = Join-Path $INFRA_DIR "seed\rinse-and-seed.bat"
    if (Test-Path $seedScript) {
        Push-Location (Join-Path $INFRA_DIR "seed")
        & cmd /c "rinse-and-seed.bat"
        Pop-Location
        Write-Status "Database seeded" "OK"
    } else {
        Write-Status "Seed script not found: $seedScript" "FAIL"
    }
}

# ============================================================================
# MAIN FLOW
# ============================================================================

# Handle simple commands
if ($Status) {
    Show-ServiceStatus
    exit 0
}

if ($Logs) {
    Open-LogsFolder
    exit 0
}

if ($Tail) {
    Start-LogTail
    exit 0
}

if ($Kill) {
    Show-Banner
    Stop-AllServices
    exit 0
}

if ($Clean) {
    Invoke-CleanReset
    exit 0
}

if ($Infra) {
    Show-Banner
    Start-Infrastructure
    exit 0
}

# Main startup flow
Show-Banner

# CRITICAL: Stop all services FIRST so we can clear their locked log files
# This ensures fresh logs every time (per blueprint mandate)
Write-Status "Stopping any existing services..." "WAIT"
Stop-AllServices

# Now clear old logs (they're unlocked now)
if (Test-Path $LOG_DIR) {
    Remove-Item "$LOG_DIR\*" -Force -ErrorAction SilentlyContinue
}
Write-Status "Logs cleared - fresh run" "OK"
Write-Host ""

# Step 1: Infrastructure
Write-Host "  [1/4] Infrastructure" -ForegroundColor White
Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray

if (-not (Test-Port 5432)) {
    Start-Infrastructure
} else {
    Write-Status "PostgreSQL already running" "OK"
    Write-Status "Keycloak already running" "OK"
}

Write-Host ""

# Step 2: Spring Boot API
Write-Host "  [2/4] Spring Boot API" -ForegroundColor White
Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray

Start-SpringBoot

# Wait for API to be ready before starting dependent services
Write-Status "Waiting for API startup (max 120s)..." "WAIT"
for ($i = 0; $i -lt 120; $i++) {
    if (Test-Port 8080) {
        Write-Status "API ready on :8080" "OK"
        break
    }
    Start-Sleep -Seconds 1
}

Write-Host ""

# Step 3: AI Service
Write-Host "  [3/4] AI Service" -ForegroundColor White
Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray

if ($NoAI) {
    Write-Status "Skipped (--no-ai flag)" "SKIP"
} else {
    Start-AIService
}

Write-Host ""

# Step 4: Frontend
Write-Host "  [4/4] Frontend" -ForegroundColor White
Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray

if ($NoFE) {
    Write-Status "Skipped (--no-fe flag)" "SKIP"
} else {
    Start-Frontend
}

Write-Host ""

# Database seeding
if ($Seed) {
    Write-Host "  [+] Database Seeding" -ForegroundColor White
    Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray
    Invoke-DatabaseSeed
    Write-Host ""
}

# Error watcher
if ($Watch) {
    Write-Host "  [+] Error Watcher" -ForegroundColor White
    Write-Host "  ----------------------------------------------------------------" -ForegroundColor DarkGray
    Start-ErrorWatcher
    Write-Host ""
}

# Final summary
Write-Host "  ================================================================" -ForegroundColor Green
Write-Host "       Dev Mode Active!                                          " -ForegroundColor Green
Write-Host "  ================================================================" -ForegroundColor Green
Write-Host ""
Write-Host "  Services:" -ForegroundColor White
Write-Host "    API:         http://localhost:8080" -ForegroundColor Gray
Write-Host "    AI Service:  http://localhost:8090 (docs: /docs)" -ForegroundColor Gray
Write-Host "    Frontend:    http://localhost:5173" -ForegroundColor Gray
Write-Host "    Keycloak:    http://localhost:8180/admin" -ForegroundColor Gray
Write-Host ""
Write-Host "  Logs:          $LOG_DIR" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Commands:" -ForegroundColor White
Write-Host "    dev --status     Check service health" -ForegroundColor DarkGray
Write-Host "    dev --logs       Open logs folder" -ForegroundColor DarkGray
Write-Host "    dev --tail       Tail all logs live" -ForegroundColor DarkGray
Write-Host "    dev --kill       Stop all services" -ForegroundColor DarkGray
Write-Host "    dev --clean      Nuclear reset" -ForegroundColor DarkGray
Write-Host ""
