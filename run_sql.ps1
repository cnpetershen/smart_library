$MYSQL_BIN = "D:\MySQL-Server-8.0\bin\mysql.exe"
$MYSQL_PWD = "1234"
$DB_NAME = "library_db"

$cmd = "$MYSQL_BIN -u root -p$MYSQL_PWD $DB_NAME"
Invoke-Expression $cmd
