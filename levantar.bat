@echo off
echo.
echo Generando imagen...
call mvnw.cmd spring-boot:build-image

echo.
echo Levantando...
call docker-compose up