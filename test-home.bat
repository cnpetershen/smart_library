@echo off
setlocal enabledelayedexpansion

REM Get token
for /f "tokens=*" %%a in ('curl.exe -s -X POST http://localhost:5173/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"') do set LOGIN_RESP=%%a

REM Extract token
echo %LOGIN_RESP% | findstr "token" >nul
if errorlevel 1 (
  echo "Login failed"
  exit /b 1
)

REM Parse token using PowerShell
for /f %%a in ('powershell -Command "$json = Get-Content -Raw | ConvertFrom-Json; Write-Output $json.data.token"') do set TOKEN=%%a

echo === Home Stats with Token ===
curl.exe -s http://localhost:5173/api/home/statistics -H "Authorization: Bearer %TOKEN%"
echo.
echo === Home Stats WITHOUT Token ===
curl.exe -s http://localhost:5173/api/home/statistics
echo.
