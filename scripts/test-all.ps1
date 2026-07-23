[CmdletBinding()]
param([switch]$Install)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
$frontendRoot = Join-Path $projectRoot "frontend"

if ($Install -or -not (Test-Path (Join-Path $frontendRoot "node_modules"))) {
    & npm.cmd --prefix $frontendRoot ci
    if ($LASTEXITCODE -ne 0) { throw "npm ci failed." }
}

& mvn -q -f (Join-Path $projectRoot "backend\pom.xml") test-compile
if ($LASTEXITCODE -ne 0) { throw "Backend test compilation failed." }

& mvn -q -f (Join-Path $projectRoot "backend\pom.xml") test
if ($LASTEXITCODE -ne 0) { throw "Backend tests failed." }

Push-Location $frontendRoot
try {
    & .\node_modules\.bin\tsc.cmd -b --pretty false
    if ($LASTEXITCODE -ne 0) { throw "TypeScript build failed." }

    & .\node_modules\.bin\vitest.cmd run --configLoader runner
    if ($LASTEXITCODE -ne 0) { throw "Frontend tests failed." }

    & .\node_modules\.bin\vite.cmd build --configLoader runner
    if ($LASTEXITCODE -ne 0) { throw "Frontend production build failed." }
}
finally {
    Pop-Location
}

Write-Host "All ScaleCanvas checks passed." -ForegroundColor Green
