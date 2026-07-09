$version = "1.0.0"
$url = "https://github.com/sina-nakhaei/android-cli/releases/download/v$version/android-$version.zip"

$installDir = "$env:USERPROFILE\.android-cli"

Write-Host "Downloading android-cli..."

Invoke-WebRequest $url -OutFile "$env:TEMP\android-cli.zip"

Expand-Archive "$env:TEMP\android-cli.zip" -DestinationPath $installDir -Force

$bin = "$installDir\android\bin"

[Environment]::SetEnvironmentVariable(
    "Path",
    [Environment]::GetEnvironmentVariable("Path", "User") + ";$bin",
    "User"
)

Write-Host "Installed successfully."
Write-Host "Restart your terminal and run: android --help"
