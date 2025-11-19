@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "ROOT_DIR=%SCRIPT_DIR%.."
set "IMAGE_DIR=%ROOT_DIR%\client\target\agent-manager-app"
set "LAUNCHER_BAT=%IMAGE_DIR%\bin\AgentManagerApp.bat"
set "LAUNCHER_EXE=%IMAGE_DIR%\bin\AgentManagerApp.exe"
set "LAUNCHER_BIN=%IMAGE_DIR%\bin\AgentManagerApp"

if exist "%LAUNCHER_BAT%" (
    set "LAUNCHER=%LAUNCHER_BAT%"
) else if exist "%LAUNCHER_EXE%" (
    set "LAUNCHER=%LAUNCHER_EXE%"
) else if exist "%LAUNCHER_BIN%" (
    set "LAUNCHER=%LAUNCHER_BIN%"
) else (
    echo Impossibile trovare l'eseguibile del client in %IMAGE_DIR%.
    echo Esegui prima: mvnw -pl client -Pdesktop clean package -DskipTests
    exit /b 1
)

call "%LAUNCHER%" %*
