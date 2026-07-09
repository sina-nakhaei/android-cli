# 1. Dynamically fetch the latest release version tag from GitHub API
Write-Host "Checking for the latest version..." -ForegroundColor Cyan
try {
    $repo = "sina-nakhaei/android-cli"
    $apiResponse = Invoke-RestMethod -Uri "https://api.github.com/repos/$repo/releases/latest" -UseBasicParsing
    $tag = $apiResponse.tag_name # This will be "v1.0.0"
} catch {
    Write-Error "Failed to fetch the latest version from GitHub API. Defaulting to v1.0.0."
    $tag = "v1.0.0"
}

# 2. Build URLs and paths based on your specific naming convention
$url = "https://github.com/sina-nakhaei/android-cli/releases/download/$tag/android-$tag.zip"
$installDir = "$env:USERPROFILE\.android-cli"
$zipPath = "$env:TEMP\android-cli.zip"

# 3. Download the asset safely
Write-Host "Downloading android-cli ($tag)..." -ForegroundColor Cyan
Invoke-WebRequest -Uri $url -OutFile $zipPath -UseBasicParsing

# 4. Clean extract directory to prevent file conflicts
if (Test-Path $installDir) {
    Remove-Item $installDir -Recurse -Force -ErrorAction SilentlyContinue
}

Write-Host "Extracting files..." -ForegroundColor Cyan
Expand-Archive -Path $zipPath -DestinationPath $installDir -Force

# 5. Dynamically locate the 'bin' folder 
# (Handles whether the zip extracts directly or creates a nested root folder like 'android-v1.0.0')
$binFolder = Get-ChildItem -Path $installDir -Recurse -Directory -Filter "bin" | Select-Object -First 1
if ($null -eq $binFolder) {
    Write-Error "Could not find a 'bin' folder inside the extracted package."
    return
    exit
}
$bin = $binFolder.FullName

# 6. Update Environment Path cleanly without duplicates
Write-Host "Configuring system PATH..." -ForegroundColor Cyan
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($currentPath -notlike "*$bin*") {
    [Environment]::SetEnvironmentVariable("Path", $currentPath + ";$bin", "User")
}

# 7. Cleanup the downloaded zip
if (Test-Path $zipPath) { Remove-Item $zipPath -Force }

Write-Host "`nInstalled successfully!" -ForegroundColor Green
Write-Host "Please restart your terminal and run: " -NoNewline
Write-Host "android --help" -ForegroundColor Yellow
