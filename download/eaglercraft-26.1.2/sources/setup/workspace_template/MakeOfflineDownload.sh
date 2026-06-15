#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Make Offline Download
# ============================================================================
#
# This script creates a single-file offline download of the EaglerCraft
# client. It bundles the compiled JavaScript, assets, and HTML into one
# self-contained HTML file that can be opened in any modern browser without
# internet access.
#
# Usage:
#   ./MakeOfflineDownload.sh [--clean] [--no-epk] [--output FILE]
#
# Options:
#   --clean          Rebuild JavaScript before bundling
#   --no-epk         Skip EPK compilation (use existing assets.epk)
#   --output FILE    Specify output filename (default: offline_download.html)
#   --no-minify      Skip JavaScript minification step
#
# Prerequisites:
#   - Compiled JavaScript (javascript/dist/classes.js)
#   - Assets EPK (output/assets.epk) or --no-epk flag
#   - Java 25+ runtime
#   - Gradle wrapper
#
# Output:
#   - output/offline_download.html  - Single-file offline download
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
OUTPUT_FILE="offline_download.html"
CLEAN_BUILD=false
NO_EPK=false
NO_MINIFY=false

# ---- Parse Arguments ----
for arg in "$@"; do
    case "$arg" in
        --clean)
            CLEAN_BUILD=true
            ;;
        --no-epk)
            NO_EPK=true
            ;;
        --no-minify)
            NO_MINIFY=true
            ;;
        --output)
            shift
            OUTPUT_FILE="${1:-offline_download.html}"
            ;;
        --help|-h)
            echo "Usage: $0 [--clean] [--no-epk] [--output FILE] [--no-minify]"
            echo ""
            echo "Options:"
            echo "  --clean          Rebuild JavaScript before bundling"
            echo "  --no-epk         Skip EPK compilation"
            echo "  --output FILE    Output filename (default: offline_download.html)"
            echo "  --no-minify      Skip JavaScript minification"
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
echo -e "${CYAN} EaglerCraft 26.1.2 - Offline Download${NC}"
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

# ---- Rebuild JavaScript if Requested ----
if [ "$CLEAN_BUILD" = true ]; then
    echo ""
    echo -e "${YELLOW}[BUILD]${NC} Rebuilding JavaScript (clean build)..."
    ./CompileJS.sh --clean || {
        echo -e "${RED}[ERROR]${NC} JavaScript compilation failed"
        exit 1
    }
fi

# ---- Check Compiled JavaScript ----
if [ ! -f "javascript/dist/classes.js" ]; then
    echo -e "${YELLOW}[WARN]${NC} Compiled JavaScript not found. Running CompileJS..."
    ./CompileJS.sh || {
        echo -e "${RED}[ERROR]${NC} JavaScript compilation failed"
        exit 1
    }
fi

CLASSES_SIZE=$(wc -c < "javascript/dist/classes.js")
CLASSES_SIZE_MB=$(echo "scale=2; $CLASSES_SIZE / 1048576" | bc)
echo -e "${GREEN}[OK]${NC} Compiled JS found: javascript/dist/classes.js (${CLASSES_SIZE_MB} MB)"

# ---- Compile EPK Assets ----
if [ "$NO_EPK" = false ]; then
    echo ""
    echo -e "${YELLOW}[EPK]${NC} Compiling asset package..."

    if [ -f "./CompileEPK.sh" ]; then
        ./CompileEPK.sh || {
            echo -e "${RED}[ERROR]${NC} EPK compilation failed"
            exit 1
        }
    else
        echo -e "${YELLOW}[WARN]${NC} CompileEPK.sh not found, attempting Gradle task..."
        ./gradlew makeEPK 2>&1 || {
            echo -e "${RED}[ERROR]${NC} EPK compilation failed"
            exit 1
        }
    fi
fi

# ---- Check EPK Assets ----
if [ ! -f "output/assets.epk" ]; then
    echo -e "${YELLOW}[WARN]${NC} Assets EPK not found: output/assets.epk"
    echo -e "${YELLOW}         Offline download will not include game assets${NC}"
    HAS_EPK=false
else
    EPK_SIZE=$(wc -c < "output/assets.epk")
    EPK_SIZE_MB=$(echo "scale=2; $EPK_SIZE / 1048576" | bc)
    echo -e "${GREEN}[OK]${NC} Assets EPK found: output/assets.epk (${EPK_SIZE_MB} MB)"
    HAS_EPK=true
fi

echo ""

# ---- Build Offline Download ----
echo -e "${CYAN}[BUILD]${NC} Creating offline download..."
START_TIME=$(date +%s)

# Create output directory
mkdir -p output

# Run the MakeOfflineDownload Gradle task
./gradlew makeOfflineDownload 2>&1 || {
    echo -e "${YELLOW}[WARN]${NC} Gradle makeOfflineDownload task failed."
    echo -e "${YELLOW}         Attempting manual assembly...${NC}"

    # Manual assembly fallback
    # Read the offline download template
    if [ ! -f "javascript/OfflineDownloadTemplate.txt" ]; then
        echo -e "${RED}[ERROR]${NC} Offline download template not found"
        exit 1
    fi

    # Build the offline HTML manually
    BUILD_DATE=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
    BUILD_HASH="local-$(date +%s)"

    # Read classes.js content
    CLASSES_JS=$(base64 -w 0 "javascript/dist/classes.js" 2>/dev/null || base64 "javascript/dist/classes.js")

    # Read EPK content if available
    EPK_B64=""
    if [ "$HAS_EPK" = true ]; then
        EPK_B64=$(base64 -w 0 "output/assets.epk" 2>/dev/null || base64 "output/assets.epk")
    fi

    # Assemble the offline download HTML
    # This creates a minimal standalone HTML with embedded JS and assets
    cat > "output/${OUTPUT_FILE}" << HTMLEOF
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="description" content="EaglerCraft 26.1.2 - Offline Edition">
    <meta name="theme-color" content="#1a1a2e">
    <title>EaglerCraft 26.1.2 - Offline</title>
    <style>
        *, *::before, *::after { margin: 0; padding: 0; box-sizing: border-box; }
        html, body { width: 100%; height: 100%; overflow: hidden; background: #1a1a2e; }
        #eaglercraft { display: block; width: 100vw; height: 100vh; image-rendering: pixelated; }
        #loading { position: fixed; inset: 0; background: #1a1a2e; display: flex; flex-direction: column; align-items: center; justify-content: center; color: #e0e0e0; font-family: sans-serif; z-index: 1000; }
        .ld-title { font-size: 2rem; color: #e94560; margin-bottom: 1rem; }
        .ld-bar { width: 300px; height: 4px; background: rgba(255,255,255,0.1); border-radius: 2px; overflow: hidden; }
        .ld-fill { height: 100%; background: #e94560; width: 0%; transition: width 0.3s; }
    </style>
</head>
<body>
    <div id="loading">
        <div class="ld-title">EaglerCraft 26.1.2</div>
        <div class="ld-bar"><div class="ld-fill" id="fill"></div></div>
    </div>
    <canvas id="eaglercraft"></canvas>
    <script type="text/javascript">
        window.eaglercraftXOpts = {
            container: "eaglercraft",
            assetsURI: "${HAS_EPK:+data:application/epk;base64,}${EPK_B64}",
            worldsDB: "worlds_offline",
            resourcePacksDB: "resourcePacks_offline",
            servers: [],
            relays: [],
            offlineMode: true,
            allowBootMenu: false,
            allowUpdateSvc: false,
            crashOnUncaughtExceptions: true,
            enableMinceraft: true
        };
    </script>
    <script type="text/javascript" src="data:application/javascript;base64,${CLASSES_JS}"></script>
</body>
</html>
HTMLEOF

    echo -e "${GREEN}[OK]${NC} Manual assembly complete (fallback mode)"
}

END_TIME=$(date +%s)
BUILD_TIME=$((END_TIME - START_TIME))

# ---- Check Output ----
if [ ! -f "output/${OUTPUT_FILE}" ]; then
    echo -e "${RED}[ERROR]${NC} Output file not created: output/${OUTPUT_FILE}"
    exit 1
fi

OUTPUT_SIZE=$(wc -c < "output/${OUTPUT_FILE}")
OUTPUT_SIZE_MB=$(echo "scale=2; $OUTPUT_SIZE / 1048576" | bc)

echo ""
echo -e "${CYAN}========================================${NC}"
echo -e "${GREEN} Offline Download Complete!${NC}"
echo -e "${CYAN}========================================${NC}"
echo -e "  Output:   output/${OUTPUT_FILE}"
echo -e "  Size:     ${OUTPUT_SIZE_MB} MB"
echo -e "  Time:     ${BUILD_TIME}s"
echo -e "  Assets:   $([ "$HAS_EPK" = true ] && echo "Included" || echo "Not included")"
echo ""
echo -e "  Open the file in any modern browser to play offline!"
echo ""
