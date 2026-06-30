@echo off
echo === Login via Vite Proxy ===
curl.exe -s -X POST http://localhost:5173/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"
echo.
echo.
echo === Login direct to backend ===
curl.exe -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"
echo.
