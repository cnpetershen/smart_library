@echo off
echo === Test Hello ===
curl.exe -s http://localhost:5173/api/test/hello
echo.
echo === Test Login ===
curl.exe -s -X POST http://localhost:5173/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"
echo.
echo === Frontend HTML ===
curl.exe -s http://localhost:5173/ | findstr "<title>"
