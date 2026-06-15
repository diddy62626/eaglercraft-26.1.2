#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Compile TeaVM JavaScript
# ============================================================================
#
# This script compiles the EaglerCraft Java source code into JavaScript
# using the TeaVM compiler. The output is placed in the javascript/ directory
# and then copied to javascript/dist/ for distribution.
#
# Usage:
#   ./CompileJS.sh [--clean] [--debug] [--no-optimize]
#
# Options:
#   --clean        Clean build artifacts before compiling
#   --debug        Build with debug information (source maps, no obfuscation)
#   --no-optimize  Disable TeaVM optimizations for faster compile times
#
# Prerequisites:
#   - Java 25+ (with preview features enabled)
#   - Gradle wrapper (gradlew) in the workspace root
#   - All source files in src/ directories
#   - minecraft-26.1.2.jar in libs/
#
# Output:
#   - javascript/classes.js       - Compiled TeaVM JavaScript
#   - javascript/classes.js.map   - Source map (if enabled)
#   - javascript/dist/classes.js  - Distribution copy
#
# ============================================================================

set -euo pipefail

# ---- Script Directory ----
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# ---- Colors for Output ----
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# ---- Parse Arguments ----
CLEAN_BUILD=false
DEBUG_BUILD=false
NO_OPTIMIZE=false

for arg in "$@"; do
    case "$arg" in
        --clean)
            CLEAN_BUILD=true
            ;;
        --debug)
            DEBUG_BUILD=true
            ;;
        --no-optimize)
            NO_OPTIMIZE=true
            ;;
        --help|-h)
            echo "Usage: $0 [--clean] [--debug] [--no-optimize]"
            echo ""
            echo "Options:"
            echo "  --clean        Clean build artifacts before compiling"
            echo "  --debug        Build with debug info (source maps, no obfuscation)"
            echo "  --no-optimize  Disable TeaVM optimizations"
            exit 0
            ;;
        *)
            echo -e "${RED}Unknown option: $arg${NC}"
            exit 1
            ;;
    esac
done

# ---- Banner ----
echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} EaglerCraft 26.1.2 - CompileJS${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# ---- Check Prerequisites ----
echo -e "${YELLOW}[CHECK]${NC} Verifying prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ERROR]${NC} Java is not installed or not in PATH"
    echo "        Please install Java 25+ and try again"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
echo -e "${GREEN}[OK]${NC} Java version: $JAVA_VERSION"

# Check Gradle wrapper
if [ ! -f "gradlew" ]; then
    echo -e "${RED}[ERROR]${NC} Gradle wrapper (gradlew) not found!"
    echo "        Run build_workspace.sh first to set up the workspace"
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Gradle wrapper found"

# Check source directories
if [ ! -d "src/teavm/java" ]; then
    echo -e "${RED}[ERROR]${NC} TeaVM source directory not found: src/teavm/java"
    echo "        Ensure the workspace is properly set up"
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Source directories present"

# Check Minecraft jar
if [ ! -f "libs/minecraft-26.1.2.jar" ]; then
    echo -e "${YELLOW}[WARN]${NC} Minecraft jar not found: libs/minecraft-26.1.2.jar"
    echo "        Compilation may fail without it"
fi

echo ""

# ---- Apply Build Mode ----
if [ "$DEBUG_BUILD" = true ]; then
    echo -e "${YELLOW}[CONFIG]${NC} Debug build enabled (source maps, no obfuscation)"
    # Modify gradle properties for debug build
    export GRADLE_OPTS="-Deaglercraft.buildMode=debug"
fi

if [ "$NO_OPTIMIZE" = true ]; then
    echo -e "${YELLOW}[CONFIG]${NC} Optimizations disabled"
    export GRADLE_OPTS="${GRADLE_OPTS:-} -Deaglercraft.optimize=false"
fi

# ---- Clean Build ----
if [ "$CLEAN_BUILD" = true ]; then
    echo -e "${YELLOW}[CLEAN]${NC} Cleaning build artifacts..."
    ./gradlew clean 2>&1 || {
        echo -e "${RED}[ERROR]${NC} Clean failed"
        exit 1
    }
    echo -e "${GREEN}[OK]${NC} Clean complete"
    echo ""
fi

# ---- Compile TeaVM JavaScript ----
echo -e "${CYAN}[COMPILE]${NC} Running TeaVM compilation..."
echo -e "${CYAN}         This may take several minutes on first build...${NC}"
echo ""

START_TIME=$(date +%s)

./gradlew teavm.js 2>&1 || {
    echo ""
    echo -e "${RED}[ERROR]${NC} TeaVM compilation failed!"
    echo -e "${RED}         Check the output above for errors${NC}"
    exit 1
}

END_TIME=$(date +%s)
COMPILE_TIME=$((END_TIME - START_TIME))

echo ""
echo -e "${GREEN}[OK]${NC} TeaVM compilation complete (${COMPILE_TIME}s)"

# ---- Check Output ----
if [ ! -f "javascript/classes.js" ]; then
    echo -e "${RED}[ERROR]${NC} Output file not found: javascript/classes.js"
    echo -e "${RED}         Compilation may have silently failed${NC}"
    exit 1
fi

CLASSES_SIZE=$(wc -c < "javascript/classes.js")
CLASSES_SIZE_MB=$(echo "scale=2; $CLASSES_SIZE / 1048576" | bc)
echo -e "${GREEN}[OK]${NC} Output: javascript/classes.js (${CLASSES_SIZE_MB} MB)"

if [ -f "javascript/classes.js.map" ]; then
    MAP_SIZE=$(wc -c < "javascript/classes.js.map")
    MAP_SIZE_MB=$(echo "scale=2; $MAP_SIZE / 1048576" | bc)
    echo -e "${GREEN}[OK]${NC} Source map: javascript/classes.js.map (${MAP_SIZE_MB} MB)"
fi

# ---- Copy to Distribution Directory ----
echo ""
echo -e "${CYAN}[COPY]${NC} Copying to distribution directory..."

mkdir -p javascript/dist

cp javascript/classes.js javascript/dist/classes.js
if [ -f "javascript/classes.js.map" ]; then
    cp javascript/classes.js.map javascript/dist/classes.js.map
fi

echo -e "${GREEN}[OK]${NC} Distribution files copied to javascript/dist/"

# ---- Summary ----
echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${GREEN} CompileJS Complete!${NC}"
echo -e "${CYAN}========================================${NC}"
echo -e "  Output:     javascript/dist/classes.js"
echo -e "  Size:       ${CLASSES_SIZE_MB} MB"
echo -e "  Build time: ${COMPILE_TIME}s"
echo -e "  Mode:       $([ "$DEBUG_BUILD" = true ] && echo "Debug" || echo "Release")"
echo ""
echo -e "  Next steps:"
echo -e "    - Run ${YELLOW}./MakeOfflineDownload.sh${NC} to create offline download"
echo -e "    - Run ${YELLOW}./CompileEPK.sh${NC} to compile asset packages"
echo -e "    - Open ${YELLOW}javascript/index.html${NC} in a browser to test"
echo ""
