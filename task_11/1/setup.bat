@echo off

set DB_NAME=bookstore
set USER=postgres

echo Creating database %DB_NAME%...
createdb -U %USER% %DB_NAME%

echo Running database.sql ...
psql -U %USER% -d %DB_NAME% -f database.sql

echo Setup complete!
pause
