# Move stock images from res/stock to assets/stock and normalize filenames
# Usage: run from repository root in PowerShell (Windows)

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
$projectRoot = Resolve-Path "$root\.."
Set-Location $projectRoot

$src = Join-Path $projectRoot 'app\src\main\res\stock'
$dstDir = Join-Path $projectRoot 'app\src\main\assets\stock'

if (-Not (Test-Path $src)) {
    Write-Host "Source folder not found: $src" -ForegroundColor Yellow
    exit 1
}

New-Item -ItemType Directory -Path $dstDir -Force | Out-Null

Get-ChildItem -Path $src -File | ForEach-Object {
    $orig = $_.Name
    # normalize: lowercase and replace non [a-z0-9._-] with underscore
    $normalized = $orig.ToLower() -replace '[^a-z0-9._-]','_'
    $normalized = $normalized -replace '_+','_'
    $dst = Join-Path $dstDir $normalized
    Copy-Item -Path $_.FullName -Destination $dst -Force
    Write-Host "Copied $orig -> $normalized"
}

# Optionally remove the res/stock files after copying
$remove = Read-Host "Remove original files under res/stock? (y/N)"
if ($remove -eq 'y' -or $remove -eq 'Y') {
    Remove-Item -Path $src -Recurse -Force
    Write-Host "Removed $src"
} else {
    Write-Host "Left original files in place. You can remove them manually when ready."
}

Write-Host "Done. You can now run: .\\gradlew clean assembleDebug" -ForegroundColor Green
