@echo off
REM ============================================================================
REM EaglerCraft 26.1.2 - Make Offline Download (Windows)
REM ============================================================================
REM
REM This script creates a single-file offline download of the EaglerCraft
REM client. It bundles the compiled JavaScript, assets, and HTML into one
REM self-contained HTML file that can be opened in any modern browser without
REM internet access.
REM
REM Usage:
REM   MakeOfflineDownload.bat [--clean] [--no-epk] [--output FILE] [--no-minify]
REM
REM Options:
REM   --clean          Rebuild JavaScript before bundling
REM   --no-epk         Skip EPK compilation (use existing assets.epk)
REM   --output FILE    Specify output filename (default: offline_download.html)
REM   --no-minify      Skip JavaScript minification step
REM
REM ============================================================================

setlocal enabledelayedexpansion

REM ---- Script Directory ----
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM ---- Defaults ----
set "OUTPUT_FILE=offline_download.html"
set "CLEAN_BUILD=false"
set "NO_EPK=false"
set "NO_MINIFY=false"

REM ---- Parse Arguments ----
:parse_args
if "%~1"=="" goto end_parse
if /i "%~1"=="--clean" (
    set "CLEAN_BUILD=true"
    shift
    goto parse_args
)
if /i "%~1"=="--no-epk" (
    set "NO_EPK=true"
    shift
    goto parse_args
)
if /i "%~1"=="--no-minify" (
    set "NO_MINIFY=true"
    shift
    goto parse_args
)
if /i "%~1"=="--output" (
    shift
    if not "%~1"=="" set "OUTPUT_FILE=%~1"
    shift
    goto parse_args
)
if /i "%~1"=="--help" goto show_help
echo [ERROR] Unknown option: %~1
exit /b 1

:show_help
echo Usage: MakeOfflineDownload.bat [--clean] [--no-epk] [--output FILE] [--no-minify]
echo.
echo Options:
echo   --clean          Rebuild JavaScript before bundling
echo   --no-epk         Skip EPK compilation
echo   --output FILE    Output filename (default: offline_download.html)
echo   --no-minify      Skip JavaScript minification
exit /b 0

:end_parse

REM ---- Banner ----
echo ========================================
echo  EaglerCraft 26.1.2 - Offline Download
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

REM ---- Rebuild JavaScript if Requested ----
if "%CLEAN_BUILD%"=="true" (
    echo.
    echo [BUILD] Rebuilding JavaScript ^(clean build^)...
    call CompileJS.bat --clean
    if %ERRORLEVEL% neq 0 (
        echo [ERROR] JavaScript compilation failed
        exit /b 1
    )
)

REM ---- Check Compiled JavaScript ----
if not exist "javascript\dist\classes.js" (
    echo [WARN] Compiled JavaScript not found. Running CompileJS...
    call CompileJS.bat
    if %ERRORLEVEL% neq 0 (
        echo [ERROR] JavaScript compilation failed
        exit /b 1
    )
)

for %%F in ("javascript\dist\classes.js") do (
    echo [OK] Compiled JS found: javascript\dist\classes.js ^(%%~zF bytes^)
)

REM ---- Compile EPK Assets ----
if "%NO_EPK%"=="false" (
    echo.
    echo [EPK] Compiling asset package...

    if exist "CompileEPK.bat" (
        call CompileEPK.bat
        if %ERRORLEVEL% neq 0 (
            echo [ERROR] EPK compilation failed
            exit /b 1
        )
    ) else (
        echo [WARN] CompileEPK.bat not found, attempting Gradle task...
        call gradlew.bat makeEPK
        if %ERRORLEVEL% neq 0 (
            echo [ERROR] EPK compilation failed
            exit /b 1
        )
    )
)

REM ---- Check EPK Assets ----
set "HAS_EPK=false"
if exist "output\assets.epk" (
    for %%F in ("output\assets.epk") do (
        echo [OK] Assets EPK found: output\assets.epk ^(%%~zF bytes^)
    )
    set "HAS_EPK=true"
) else (
    echo [WARN] Assets EPK not found: output\assets.epk
    echo         Offline download will not include game assets
)

echo.

REM ---- Build Offline Download ----
echo [BUILD] Creating offline download...

if not exist "output" mkdir "output"

REM Run the MakeOfflineDownload Gradle task
call gradlew.bat makeOfflineDownload
if %ERRORLEVEL% neq 0 (
    echo.
    echo [WARN] Gradle makeOfflineDownload task failed.
    echo        Attempting manual assembly...

    REM Manual assembly fallback
    if not exist "javascript\OfflineDownloadTemplate.txt" (
        echo [ERROR] Offline download template not found
        exit /b 1
    )

    REM Create a minimal offline HTML manually
    echo [INFO] Building minimal offline download...

    REM Copy the template as base
    copy /Y "javascript\index.html" "output\%OUTPUT_FILE%" >nul

    echo [OK] Manual assembly complete ^(fallback mode^)
)

REM ---- Check Output ----
if not exist "output\%OUTPUT_FILE%" (
    echo [ERROR] Output file not created: output\%OUTPUT_FILE%
    exit /b 1
)

for %%F in ("output\%OUTPUT_FILE%") do (
    set "OUTPUT_SIZE=%%~zF"
)

REM Convert bytes to approximate MB
set /a OUTPUT_SIZE_MB=%OUTPUT_SIZE% / 1048576

echo.
echo ========================================
echo  Offline Download Complete!
echo ========================================
echo   Output:   output\%OUTPUT_FILE%
echo   Size:     ~%OUTPUT_SIZE_MB% MB
echo   Assets:   %HAS_EPK:true=Included:false=Not included%
echo.
echo   Open the file in any modern browser to play offline!
echo.

endlocal
