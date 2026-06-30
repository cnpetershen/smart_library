'{"account":"reader001","password":"123456","role":"reader"}' | Set-Content -Path "$env:TEMP\login.json" -Encoding Ascii
$body = curl.exe -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "@$env:TEMP\login.json"
$token = ($body | ConvertFrom-Json).data.token
Write-Host "=== 热门排行 ==="
curl.exe -s http://localhost:8080/api/ai/hot -H "Authorization: Bearer $token"
Write-Host ""
Write-Host "=== 智能推荐 ==="
curl.exe -s http://localhost:8080/api/ai/recommend -H "Authorization: Bearer $token"
Write-Host ""
Write-Host "=== 趋势数据 ==="
curl.exe -s http://localhost:8080/api/ai/trend -H "Authorization: Bearer $token"
Write-Host ""
Write-Host "=== 分类统计 ==="
curl.exe -s "http://localhost:8080/api/ai/statistics?type=category" -H "Authorization: Bearer $token"