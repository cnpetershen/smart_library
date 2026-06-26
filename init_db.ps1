$MYSQL_BIN = "D:\MySQL-Server-8.0\bin\mysql.exe"
$MYSQL_PWD = "1234"

$sql = Get-Content "c:\项目\智慧图书馆\schema_init.sql" -Raw
$sql | & $MYSQL_BIN -u root -p$MYSQL_PWD library_db 2>&1
