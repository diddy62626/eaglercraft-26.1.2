@echo off
REM ============================================================================
REM EaglerCraft 26.1.2 - Compile EPK Assets (Windows)
REM ============================================================================
REM
REM This script compiles the EaglerCraft game assets (textures, sounds, models,
REM lang files, etc.) into an EPK (EaglerCraft Package) file. The EPK format
REM is a custom binary archive optimized for fast loading in the browser.
REM
REM Usage:
REM   CompileEPK.bat [--clean] [--output FILE] [--compress LEVEL] [--verbose]
REM
REM Options:
REM   --clean           Clean output directory before compiling
REM   --output FILE     Output filename (default: assets.epk)
REM   --compress LEVEL  Compression level 0-9 (default: 6)
REM   --verbose         Show detailed compilation output
REM
REM ============================================================================

setlocal enabledelayedexpansion

REM ---- Script Directory ----
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM ---- Defaults ----
set "OUTPUT_FILE=assets.epk"
set "CLEAN_BUILD=false"
set "COMPRESS_LEVEL=6"
set "VERBOSE=false"

REM ---- Parse Arguments ----
:parse_args
if "%~1"=="" goto end_parse
if /i "%~1"=="--clean" (
    set "CLEAN_BUILD=true"
    shift
    goto parse_args
)
if /i "%~1"=="--output" (
    shift
    if not "%~1"=="" set "OUTPUT_FILE=%~1"
    shift
    goto parse_args
)
if /i "%~1"=="--compress" (
    shift
    if not "%~1"=="" set "COMPRESS_LEVEL=%~1"
    shift
    goto parse_args
)
if /i "%~1"=="--verbose" (
    set "VERBOSE=true"
    shift
    goto parse_args
)
if /i "%~1"=="--help" goto show_help
echo [ERROR] Unknown option: %~1
exit /b 1

:show_help
echo Usage: CompileEPK.bat [--clean] [--output FILE] [--compress LEVEL] [--verbose]
echo.
echo Options:
echo   --clean           Clean output before compiling
echo   --output FILE     Output filename (default: assets.epk)
echo   --compress LEVEL  Compression level 0-9 (default: 6)
echo   --verbose         Show detailed output
exit /b 0

:end_parse

REM ---- Banner ----
echo ========================================
echo  EaglerCraft 26.1.2 - Compile EPK
echo ========================================
echo.

REM ---- Check Prerequisites ----
echo [CHECK] Verifying prerequisites...

where java >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    exit /b 1
)
echo [OK] Java found

if not exist "gradlew.bat" (
    echo [ERROR] Gradle wrapper not found. Run build_workspace.sh first.
    exit /b 1
)
echo [OK] Gradle wrapper found

if not exist "src\resources" (
    echo [ERROR] Resources directory not found: src\resources
    echo         Ensure game assets are in place
    exit /b 1
)
echo [OK] Resources directory found

REM Count resource files (rough estimate)
set "RESOURCE_COUNT=0"
for /r "src\resources" %%F in (*) do set /a RESOURCE_COUNT+=1
echo [OK] Resource files: %RESOURCE_COUNT%

if %RESOURCE_COUNT%==0 (
    echo [WARN] No resource files found in src\resources\
    echo         EPK will be empty
)

echo.

REM ---- Clean Output ----
if "%CLEAN_BUILD%"=="true" (
    echo [CLEAN] Cleaning output directory...
    if exist "output\assets.epk" del "output\assets.epk"
    echo [OK] Clean complete
    echo.
)

REM ---- Create Output Directory ----
if not exist "output" mkdir "output"

REM ---- Compile EPK ----
echo [COMPILE] Running EPK compiler...
echo           Source: src\resources\
echo           Output: output\%OUTPUT_FILE%
echo           Compression: level %COMPRESS_LEVEL%
echo.

REM Run the Gradle makeEPK task
call gradlew.bat makeEPK
if %ERRORLEVEL% neq 0 (
    echo.
    echo [WARN] Gradle makeEPK task failed.
    echo        Attempting direct Java execution...

    REM Fallback: Run the EPK compiler directly
    if exist "build\classes\java\main\net\lax1dude\eaglercraft\v2_6\gui\EPKCompiler.class" (
        java -cp "build\classes\java\main;libs\minecraft-26.1.2.jar" --enable-preview net.lax1dude.eaglercraft.v2_6.gui.EPKCompiler "src\resources" "output\%OUTPUT_FILE%"
        if %ERRORLEVEL% neq 0 (
            echo [ERROR] EPK compilation failed
            exit /b 1
        )
    ) else (
        echo [ERROR] EPK compiler class not found
        echo         Build the project first with CompileJS.bat
        exit /b 1
    )
)

echo.
echo [OK] EPK compilation complete

REM ---- Check Output ----
if not exist "output\%OUTPUT_FILE%" (
    echo [ERROR] Output file not found: output\%OUTPUT_FILE%
    exit /b 1
)

for %%F in ("output\%OUTPUT_FILE%") do (
    set "EPK_SIZE=%%~zF"
)

set /a EPK_SIZE_MB=%EPK_SIZE% / 1048576

echo [OK] Output: output\%OUTPUT_FILE% ^(~%EPK_SIZE_MB% MB^)

REM ---- Summary ----
echo.
echo ========================================
echo  CompileEPK Complete!
echo ========================================
echo   Output:     output\%OUTPUT_FILE%
echo   Size:       ~%EPK_SIZE_MB% MB
echo   Files:      %RESOURCE_COUNT% resources
echo   Compress:   Level %COMPRESS_LEVEL%
echo.
echo   Next steps:
echo     - Run MakeOfflineDownload.bat to bundle with offline download
echo     - Copy to javascript\ for web server deployment
echo     - Run CompileJS.bat to compile JavaScript (if not done)
echo.

endlocal
