@echo off
echo === Test 1: Vite serves HTML ===
curl.exe -s -o NUL -w "HTTP %%{http_code}" http://localhost:5173/
echo.
echo === Test 2: Login via Vite proxy ===
curl.exe -s -o NUL -w "HTTP %%{http_code}" -X POST http://localhost:5173/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"
echo.
echo === Test 3: Login direct backend ===
curl.exe -s -o NUL -w "HTTP %%{http_code}" -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"account\":\"admin\",\"password\":\"admin123\",\"role\":\"admin\"}"
echo.
echo === Test 4: Vite proxy check ===
curl.exe -s -o NUL -w "HTTP %%{http_code}" http://localhost:5173/api/test/hello
echo.
