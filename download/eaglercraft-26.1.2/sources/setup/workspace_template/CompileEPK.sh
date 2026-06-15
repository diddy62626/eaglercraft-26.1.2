#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Compile EPK Assets
# ============================================================================
#
# This script compiles the EaglerCraft game assets (textures, sounds, models,
# lang files, etc.) into an EPK (EaglerCraft Package) file. The EPK format
# is a custom binary archive optimized for fast loading in the browser.
#
# Usage:
#   ./CompileEPK.sh [--clean] [--output FILE] [--compress LEVEL]
#
# Options:
#   --clean           Clean output directory before compiling
#   --output FILE     Output filename (default: assets.epk)
#   --compress LEVEL  Compression level 0-9 (default: 6)
#   --verbose         Show detailed compilation output
#
# Prerequisites:
#   - Java 25+ runtime
#   - Gradle wrapper
#   - Asset files in src/resources/
#
# Output:
#   - output/assets.epk  - Compiled asset package
#
# EPK File Format:
#   - Header: Magic bytes "EAGL" + version + flags
#   - Entries: Filename (UTF-8) + compressed data blocks
#   - Compression: Deflate (zlib) per-entry
#   - Footer: CRC32 checksum + entry count
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
NC='\033[0m'

# ---- Defaults ----
OUTPUT_FILE="assets.epk"
CLEAN_BUILD=false
COMPRESS_LEVEL=6
VERBOSE=false

# ---- Parse Arguments ----
for arg in "$@"; do
    case "$arg" in
        --clean)
            CLEAN_BUILD=true
            ;;
        --output)
            shift
            OUTPUT_FILE="${1:-assets.epk}"
            ;;
        --compress)
            shift
            COMPRESS_LEVEL="${1:-6}"
            # Validate compression level
            if ! [[ "$COMPRESS_LEVEL" =~ ^[0-9]$ ]] || [ "$COMPRESS_LEVEL" -lt 0 ] || [ "$COMPRESS_LEVEL" -gt 9 ]; then
                echo -e "${RED}[ERROR]${NC} Compression level must be 0-9, got: $COMPRESS_LEVEL"
                exit 1
            fi
            ;;
        --verbose)
            VERBOSE=true
            ;;
        --help|-h)
            echo "Usage: $0 [--clean] [--output FILE] [--compress LEVEL] [--verbose]"
            echo ""
            echo "Options:"
            echo "  --clean           Clean output before compiling"
            echo "  --output FILE     Output filename (default: assets.epk)"
            echo "  --compress LEVEL  Compression level 0-9 (default: 6)"
            echo "  --verbose         Show detailed output"
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
echo -e "${CYAN} EaglerCraft 26.1.2 - Compile EPK${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# ---- Check Prerequisites ----
echo -e "${YELLOW}[CHECK]${NC} Verifying prerequisites..."

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ERROR]${NC} Java is not installed or not in PATH"
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Java found"

# Check Gradle wrapper
if [ ! -f "gradlew" ]; then
    echo -e "${RED}[ERROR]${NC} Gradle wrapper not found. Run build_workspace.sh first."
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Gradle wrapper found"

# Check resources directory
if [ ! -d "src/resources" ]; then
    echo -e "${RED}[ERROR]${NC} Resources directory not found: src/resources"
    echo -e "${RED}         Ensure game assets are in place${NC}"
    exit 1
fi
echo -e "${GREEN}[OK]${NC} Resources directory found"

# Count resource files
RESOURCE_COUNT=$(find src/resources -type f | wc -l | tr -d ' ')
echo -e "${GREEN}[OK]${NC} Resource files: ${RESOURCE_COUNT}"

if [ "$RESOURCE_COUNT" -eq 0 ]; then
    echo -e "${YELLOW}[WARN]${NC} No resource files found in src/resources/"
    echo -e "${YELLOW}         EPK will be empty${NC}"
fi

echo ""

# ---- Clean Output ----
if [ "$CLEAN_BUILD" = true ]; then
    echo -e "${YELLOW}[CLEAN]${NC} Cleaning output directory..."
    rm -rf output/assets.epk
    echo -e "${GREEN}[OK]${NC} Clean complete"
    echo ""
fi

# ---- Create Output Directory ----
mkdir -p output

# ---- Compile EPK ----
echo -e "${CYAN}[COMPILE]${NC} Running EPK compiler..."
echo -e "${CYAN}          Source: src/resources/${NC}"
echo -e "${CYAN}          Output: output/${OUTPUT_FILE}${NC}"
echo -e "${CYAN}          Compression: level ${COMPRESS_LEVEL}${NC}"
echo ""

START_TIME=$(date +%s)

# Run the Gradle makeEPK task with arguments
# The Gradle task calls EPKCompiler with the configured arguments
./gradlew makeEPK --args="src/resources output/${OUTPUT_FILE}" 2>&1 || {
    echo ""
    echo -e "${YELLOW}[WARN]${NC} Gradle makeEPK task failed."
    echo -e "${YELLOW}         Attempting direct Java execution...${NC}"

    # Fallback: Run the EPK compiler directly
    # This requires the compiled classes to be available
    if [ -f "build/classes/java/main/net/lax1dude/eaglercraft/v2_6/gui/EPKCompiler.class" ]; then
        java \
            -cp "build/classes/java/main:libs/minecraft-26.1.2.jar" \
            --enable-preview \
            net.lax1dude.eaglercraft.v2_6.gui.EPKCompiler \
            "src/resources" "output/${OUTPUT_FILE}" 2>&1 || {
            echo -e "${RED}[ERROR]${NC} EPK compilation failed"
            exit 1
        }
    else
        echo -e "${RED}[ERROR]${NC} EPK compiler class not found"
        echo -e "${RED}         Build the project first with ./CompileJS.sh${NC}"
        exit 1
    fi
}

END_TIME=$(date +%s)
COMPILE_TIME=$((END_TIME - START_TIME))

echo ""
echo -e "${GREEN}[OK]${NC} EPK compilation complete (${COMPILE_TIME}s)"

# ---- Check Output ----
if [ ! -f "output/${OUTPUT_FILE}" ]; then
    echo -e "${RED}[ERROR]${NC} Output file not found: output/${OUTPUT_FILE}"
    exit 1
fi

EPK_SIZE=$(wc -c < "output/${OUTPUT_FILE}")
EPK_SIZE_MB=$(echo "scale=2; $EPK_SIZE / 1048576" | bc)

echo -e "${GREEN}[OK]${NC} Output: output/${OUTPUT_FILE} (${EPK_SIZE_MB} MB)"

# ---- Validate EPK ----
echo ""
echo -e "${YELLOW}[VALIDATE]${NC} Verifying EPK file..."

# Check magic bytes (EAGL)
EPK_MAGIC=$(xxd -l 4 -p "output/${OUTPUT_FILE}" 2>/dev/null || echo "unknown")
if [ "$EPK_MAGIC" = "4541474c" ]; then
    echo -e "${GREEN}[OK]${NC} Valid EPK header (EAGL magic bytes)"
elif [ "$EPK_MAGIC" = "504b0304" ]; then
    echo -e "${YELLOW}[WARN]${NC} File appears to be a ZIP archive (PK header)"
    echo -e "${YELLOW}         This may indicate the EPK compiler produced a ZIP-compatible format${NC}"
else
    echo -e "${YELLOW}[WARN]${NC} Unexpected file header: ${EPK_MAGIC}"
    echo -e "${YELLOW}         The file may not be a valid EPK${NC}"
fi

# ---- Summary ----
echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${GREEN} CompileEPK Complete!${NC}"
echo -e "${CYAN}========================================${NC}"
echo -e "  Output:     output/${OUTPUT_FILE}"
echo -e "  Size:       ${EPK_SIZE_MB} MB"
echo -e "  Files:      ${RESOURCE_COUNT} resources"
echo -e "  Compress:   Level ${COMPRESS_LEVEL}"
echo -e "  Build time: ${COMPILE_TIME}s"
echo ""
echo -e "  Next steps:"
echo -e "    - Run ${YELLOW}./MakeOfflineDownload.sh${NC} to bundle with offline download"
echo -e "    - Copy to ${YELLOW}javascript/${NC} for web server deployment"
echo -e "    - Run ${YELLOW}./CompileJS.sh${NC} to compile JavaScript (if not done)"
echo ""
