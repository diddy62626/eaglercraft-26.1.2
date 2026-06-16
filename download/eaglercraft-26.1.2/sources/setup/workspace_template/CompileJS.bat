@echo off
REM ============================================================================
REM EaglerCraft 26.1.2 - Compile TeaVM JavaScript (Windows)
REM ============================================================================
REM
REM This script compiles the EaglerCraft Java source code into JavaScript
REM using the TeaVM compiler. The output is placed in the javascript/ directory
REM and then copied to javascript/dist/ for distribution.
REM
REM Usage:
REM   CompileJS.bat [--clean] [--debug] [--no-optimize]
REM
REM Options:
REM   --clean        Clean build artifacts before compiling
REM   --debug        Build with debug information (source maps, no obfuscation)
REM   --no-optimize  Disable TeaVM optimizations for faster compile times
REM
REM Prerequisites:
REM   - Java 25+ (with preview features enabled)
REM   - Gradle wrapper (gradlew.bat) in the workspace root
REM   - All source files in src/ directories
REM   - minecraft-26.1.2.jar in libs/
REM
REM ============================================================================

setlocal enabledelayedexpansion

REM ---- Script Directory ----
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM ---- Parse Arguments ----
set "CLEAN_BUILD=false"
set "DEBUG_BUILD=false"
set "NO_OPTIMIZE=false"

:parse_args
if "%~1"=="" goto end_parse
if /i "%~1"=="--clean" (
    set "CLEAN_BUILD=true"
    shift
    goto parse_args
)
if /i "%~1"=="--debug" (
    set "DEBUG_BUILD=true"
    shift
    goto parse_args
)
if /i "%~1"=="--no-optimize" (
    set "NO_OPTIMIZE=true"
    shift
    goto parse_args
)
if /i "%~1"=="--help" goto show_help
echo [ERROR] Unknown option: %~1
exit /b 1

:show_help
echo Usage: CompileJS.bat [--clean] [--debug] [--no-optimize]
echo.
echo Options:
echo   --clean        Clean build artifacts before compiling
echo   --debug        Build with debug info (source maps, no obfuscation)
echo   --no-optimize  Disable TeaVM optimizations
exit /b 0

:end_parse

REM ---- Banner ----
echo ========================================
echo  EaglerCraft 26.1.2 - CompileJS
echo ========================================
echo.

REM ---- Check Prerequisites ----
echo [CHECK] Verifying prerequisites...

where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo         Please install Java 25+ and try again
    exit /b 1
)

for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set "JAVA_VER=%%~v"
)
echo [OK] Java version: %JAVA_VER%

if not exist "gradlew.bat" (
    echo [ERROR] Gradle wrapper (gradlew.bat) not found!
    echo         Run build_workspace.sh first to set up the workspace
    exit /b 1
)
echo [OK] Gradle wrapper found

if not exist "src\teavm\java" (
    echo [ERROR] TeaVM source directory not found: src\teavm\java
    echo         Ensure the workspace is properly set up
    exit /b 1
)
echo [OK] Source directories present

if not exist "libs\minecraft-26.1.2.jar" (
    echo [WARN] Minecraft jar not found: libs\minecraft-26.1.2.jar
    echo         Compilation may fail without it
)

echo.

REM ---- Apply Build Mode ----
set "GRADLE_OPTS="

if "%DEBUG_BUILD%"=="true" (
    echo [CONFIG] Debug build enabled
    set "GRADLE_OPTS=-Deaglercraft.buildMode=debug"
)

if "%NO_OPTIMIZE%"=="true" (
    echo [CONFIG] Optimizations disabled
    set "GRADLE_OPTS=%GRADLE_OPTS% -Deaglercraft.optimize=false"
)

REM ---- Clean Build ----
if "%CLEAN_BUILD%"=="true" (
    echo [CLEAN] Cleaning build artifacts...
    call gradlew.bat clean
    if %ERRORLEVEL% neq 0 (
        echo [ERROR] Clean failed
        exit /b 1
    )
    echo [OK] Clean complete
    echo.
)

REM ---- Compile TeaVM JavaScript ----
echo [COMPILE] Running TeaVM compilation...
echo           This may take several minutes on first build...
echo.

set "START_TIME=%TIME%"

call gradlew.bat teavm.js
if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] TeaVM compilation failed!
    echo         Check the output above for errors
    exit /b 1
)

echo.
echo [OK] TeaVM compilation complete

REM ---- Check Output ----
if not exist "javascript\classes.js" (
    echo [ERROR] Output file not found: javascript\classes.js
    echo         Compilation may have silently failed
    exit /b 1
)

for %%F in ("javascript\classes.js") do (
    set "CLASSES_SIZE=%%~zF"
)
echo [OK] Output: javascript\classes.js (%CLASSES_SIZE% bytes)

if exist "javascript\classes.js.map" (
    for %%F in ("javascript\classes.js.map") do (
        set "MAP_SIZE=%%~zF"
    )
    echo [OK] Source map: javascript\classes.js.map (%MAP_SIZE% bytes)
)

REM ---- Copy to Distribution Directory ----
echo.
echo [COPY] Copying to distribution directory...

if not exist "javascript\dist" mkdir "javascript\dist"

copy /Y "javascript\classes.js" "javascript\dist\classes.js" >nul
if exist "javascript\classes.js.map" (
    copy /Y "javascript\classes.js.map" "javascript\dist\classes.js.map" >nul
)

echo [OK] Distribution files copied to javascript\dist\

REM ---- Summary ----
echo.
echo ========================================
echo  CompileJS Complete!
echo ========================================
echo   Output:     javascript\dist\classes.js
echo   Mode:       %DEBUG_BUILD:true=Debug:false=Release%
echo.
echo   Next steps:
echo     - Run MakeOfflineDownload.bat to create offline download
echo     - Run CompileEPK.bat to compile asset packages
echo     - Open javascript\index.html in a browser to test
echo.

endlocal
