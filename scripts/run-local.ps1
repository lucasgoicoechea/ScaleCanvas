[CmdletBinding()]
param(
    [switch]$SkipInstall,
    [switch]$NoBrowser
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$frontendRoot = Join-Path $projectRoot "frontend"
$backendRoot = Join-Path $projectRoot "backend"
$localRoot = Join-Path $projectRoot ".local"
$backendLog = Join-Path $localRoot "backend.log"
$backendError = Join-Path $localRoot "backend.err"
$frontendLog = Join-Path $localRoot "frontend.log"
$frontendError = Join-Path $localRoot "frontend.err"
$backendProcess = $null
$frontendProcess = $null

function Require-Command {
    param([string]$Name, [string]$InstallHint)

    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        throw "Missing required command '$Name'. $InstallHint"
    }
}

function Wait-ForHealth {
    param([string]$Url, [int]$TimeoutSeconds = 60)

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        try {
            $health = Invoke-RestMethod -Uri $Url -TimeoutSec 2
            if ($health.status -eq "UP") {
                return
            }
        } catch {
            Start-Sleep -Milliseconds 750
        }
    }
    throw "Backend did not become healthy within $TimeoutSeconds seconds. Review $backendLog and $backendError."
}

function Wait-ForUrl {
    param([string]$Url, [int]$TimeoutSeconds = 30)

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 2
            if ($response.StatusCode -eq 200) {
                return
            }
        } catch {
            Start-Sleep -Milliseconds 500
        }
    }
    throw "Frontend did not become available within $TimeoutSeconds seconds."
}

function Start-LocalProcess {
    param(
        [string]$FileName,
        [string[]]$Arguments,
        [string]$WorkingDirectory,
        [string]$OutputLog,
        [string]$ErrorLog
    )

    # Start-Process fails when its inherited Windows environment contains both
    # "Path" and "PATH". Starting the resolved executable directly avoids
    # rebuilding that case-insensitive environment dictionary.
    $startInfo = [System.Diagnostics.ProcessStartInfo]::new()
    $startInfo.FileName = $FileName
    $startInfo.WorkingDirectory = $WorkingDirectory
    $startInfo.UseShellExecute = $false
    $startInfo.CreateNoWindow = $true
    $startInfo.RedirectStandardOutput = $true
    $startInfo.RedirectStandardError = $true
    $startInfo.WindowStyle = [System.Diagnostics.ProcessWindowStyle]::Hidden
    $startInfo.Arguments = (($Arguments | ForEach-Object {
        '"' + $_.Replace('"', '\"') + '"'
    }) -join " ")
    $process = [System.Diagnostics.Process]::Start($startInfo)
    $process | Add-Member -NotePropertyName OutputCapture -NotePropertyValue $process.StandardOutput.ReadToEndAsync()
    $process | Add-Member -NotePropertyName ErrorCapture -NotePropertyValue $process.StandardError.ReadToEndAsync()
    $process | Add-Member -NotePropertyName OutputLog -NotePropertyValue $OutputLog
    $process | Add-Member -NotePropertyName ErrorLog -NotePropertyValue $ErrorLog
    return $process
}

function Save-ProcessOutput {
    param([System.Diagnostics.Process]$Process)

    if (-not $Process -or -not $Process.HasExited) {
        return
    }
    if ($Process.OutputCapture) {
        [System.IO.File]::WriteAllText($Process.OutputLog, $Process.OutputCapture.GetAwaiter().GetResult())
    }
    if ($Process.ErrorCapture) {
        [System.IO.File]::WriteAllText($Process.ErrorLog, $Process.ErrorCapture.GetAwaiter().GetResult())
    }
}

Require-Command "java" "Install Java 25 and make it available on PATH."
Require-Command "mvn" "Install Maven 3.9+ and make it available on PATH."
Require-Command "node" "Install Node.js 22+ and make it available on PATH."
Require-Command "npm.cmd" "Install npm with Node.js."

$javaExecutable = (Get-Command "java").Source
$nodeExecutable = (Get-Command "node").Source

New-Item -ItemType Directory -Path $localRoot -Force | Out-Null

if (-not $SkipInstall -and -not (Test-Path (Join-Path $frontendRoot "node_modules"))) {
    Write-Host "Installing frontend dependencies..."
    & npm.cmd --prefix $frontendRoot ci
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend dependency installation failed."
    }
}

Write-Host "Building backend..."
& mvn -q -f (Join-Path $backendRoot "pom.xml") package "-Dmaven.test.skip=true"
if ($LASTEXITCODE -ne 0) {
    throw "Backend build failed."
}

$backendJar = Join-Path $backendRoot "target\scalecanvas-backend-0.1.0-SNAPSHOT.jar"
$viteEntrypoint = Join-Path $frontendRoot "node_modules\vite\bin\vite.js"

if (-not (Test-Path $backendJar)) {
    throw "Backend JAR was not generated at $backendJar."
}
if (-not (Test-Path $viteEntrypoint)) {
    throw "Vite is unavailable. Run without -SkipInstall."
}

try {
    Write-Host "Starting backend with the local H2 database..."
    $backendProcess = Start-LocalProcess `
        -FileName $javaExecutable `
        -Arguments @("-jar", $backendJar, "--logging.file.name=$backendLog") `
        -WorkingDirectory $backendRoot `
        -OutputLog $backendLog `
        -ErrorLog $backendError

    Write-Host "Starting frontend..."
    $frontendProcess = Start-LocalProcess `
        -FileName $nodeExecutable `
        -Arguments @($viteEntrypoint, "--host", "127.0.0.1", "--configLoader", "runner") `
        -WorkingDirectory $frontendRoot `
        -OutputLog $frontendLog `
        -ErrorLog $frontendError

    Wait-ForHealth "http://localhost:8080/actuator/health"
    Wait-ForUrl "http://127.0.0.1:5173"

    Write-Host ""
    Write-Host "ScaleCanvas is ready." -ForegroundColor Green
    Write-Host "Application: http://127.0.0.1:5173"
    Write-Host "Swagger:     http://localhost:8080/swagger-ui.html"
    Write-Host "Health:      http://localhost:8080/actuator/health"
    Write-Host ""
    Write-Host "Press Ctrl+C to stop both processes."

    if (-not $NoBrowser) {
        Start-Process "http://127.0.0.1:5173"
    }

    while (-not $backendProcess.HasExited -and -not $frontendProcess.HasExited) {
        Start-Sleep -Seconds 1
        $backendProcess.Refresh()
        $frontendProcess.Refresh()
    }

    if ($backendProcess.HasExited) {
        throw "Backend exited unexpectedly. Review $backendError."
    }
    throw "Frontend exited unexpectedly. Review $frontendError."
}
finally {
    if ($frontendProcess -and -not $frontendProcess.HasExited) {
        Stop-Process -Id $frontendProcess.Id -Force -ErrorAction SilentlyContinue
    }
    if ($backendProcess -and -not $backendProcess.HasExited) {
        Stop-Process -Id $backendProcess.Id -Force -ErrorAction SilentlyContinue
    }
    if ($frontendProcess) {
        $frontendProcess.WaitForExit()
        Save-ProcessOutput $frontendProcess
    }
    if ($backendProcess) {
        $backendProcess.WaitForExit()
        Save-ProcessOutput $backendProcess
    }
    Write-Host "ScaleCanvas stopped."
}
