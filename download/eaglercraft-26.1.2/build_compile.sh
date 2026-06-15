#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Compile the Client
# ============================================================================
# Compiles the EaglerCraft client using TeaVM 0.15.0
# Translates Java bytecode to JavaScript for browser execution
#
# Usage:
#   ./build_compile.sh [options]
#
# Options:
#   --release          Build release version (optimized, obfuscated, minified)
#   --debug            Build debug version (with source maps, no obfuscation)
#   --development      Build development version (default)
#   --no-obfuscate     Skip TeaVM obfuscation
#   --no-source-maps   Skip source map generation
#   --no-incremental   Disable incremental compilation
#   --minify           Enable JavaScript minification
#   --heap <size>      Set max JVM heap size in MB (default: 2048)
#   --help             Show this help message
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { echo -e "${CYAN}[COMPILE]${NC} $1"; }
warn()  { echo -e "${YELLOW}[COMPILE]${NC} $1"; }
error() { echo -e "${RED}[COMPILE]${NC} $1"; }
success() { echo -e "${GREEN}[COMPILE]${NC} $1"; }

show_help() {
    echo "EaglerCraft 26.1.2 - Compile the Client"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --release          Build release version (optimized, obfuscated, minified)"
    echo "  --debug            Build debug version (with source maps, no obfuscation)"
    echo "  --development      Build development version (default)"
    echo "  --no-obfuscate     Skip TeaVM obfuscation"
    echo "  --no-source-maps   Skip source map generation"
    echo "  --no-incremental   Disable incremental compilation"
    echo "  --minify           Enable JavaScript minification"
    echo "  --heap <size>      Set max JVM heap size in MB (default: 2048)"
    echo "  --help             Show this help message"
    echo ""
    echo "Compilation Pipeline:"
    echo "  1. Compile Java source to bytecode (javac)"
    echo "  2. Run TeaVM to translate bytecode to JavaScript"
    echo "  3. Post-process output (source maps, assets)"
    echo "  4. Package assets (EPK format)"
}

main() {
    local compile_args=""
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --release|--debug|--development|--no-obfuscate|--no-source-maps|\
            --no-incremental|--minify)
                compile_args="$compile_args $1"
                shift
                ;;
            --heap)
                compile_args="$compile_args --heap $2"
                shift 2
                ;;
            --help|-h)
                show_help
                exit 0
                ;;
            *)
                warn "Unknown option: $1"
                shift
                ;;
        esac
    done
    
    echo ""
    echo "╔══════════════════════════════════════════════════════════╗"
    echo "║   EaglerCraft 26.1.2 - Compile Client                   ║"
    echo "║   TeaVM 0.15.0 - Java to JavaScript                     ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    # Check workspace
    if [ ! -d "$SCRIPT_DIR/workspace" ]; then
        error "Workspace not found! Run ./build_workspace.sh first."
        exit 1
    fi
    
    # Check workspace source
    if [ ! -d "$SCRIPT_DIR/workspace/src" ]; then
        error "No source files in workspace! Run ./build_init.sh and ./build_workspace.sh first."
        exit 1
    fi
    
    # Check Java
    if ! command -v java &> /dev/null; then
        error "Java not found! Please install Java 25 or later."
        exit 1
    fi
    
    # Determine compile mode
    local mode="development"
    if [[ "$compile_args" == *"--release"* ]]; then
        mode="release"
    elif [[ "$compile_args" == *"--debug"* ]]; then
        mode="debug"
    fi
    
    info "Compile mode: $mode"
    
    BUILD_TOOLS_JAR="$SCRIPT_DIR/buildtools/BuildTools.jar"
    
    if [ -f "$BUILD_TOOLS_JAR" ]; then
        info "Running compilation via BuildTools..."
        java -Xmx2G -jar "$BUILD_TOOLS_JAR" compile $compile_args
    else
        warn "BuildTools.jar not found, using fallback compilation..."
        fallback_compile "$mode" "$compile_args"
    fi
    
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        echo ""
        success "Compilation complete!"
        info "Output: $SCRIPT_DIR/workspace/javascript/"
        info "Run ./build_dist.sh to create distribution packages."
    else
        error "Compilation failed with exit code $EXIT_CODE"
        exit $EXIT_CODE
    fi
}

fallback_compile() {
    local mode="$1"
    local args="$2"
    
    local ws_dir="$SCRIPT_DIR/workspace"
    local bin_dir="$ws_dir/bin"
    local out_dir="$ws_dir/javascript"
    local teavm_cache="$ws_dir/.teavm"
    
    # Clean bin directory
    rm -rf "$bin_dir"
    mkdir -p "$bin_dir"
    mkdir -p "$out_dir"
    mkdir -p "$teavm_cache"
    
    # Step 1: Compile Java source
    info "Step 1: Compiling Java source to bytecode..."
    
    local source_files=()
    find "$ws_dir/src/main/java" -name '*.java' -type f 2>/dev/null | while read f; do
        source_files+=("$f")
    done
    find "$ws_dir/src/teavm/java" -name '*.java' -type f 2>/dev/null | while read f; do
        source_files+=("$f")
    done
    
    local src_count=$(find "$ws_dir/src" -name '*.java' -type f 2>/dev/null | wc -l)
    info "Found $src_count Java source files"
    
    # Build classpath
    local classpath="$bin_dir"
    if [ -d "$ws_dir/lib/teavm" ]; then
        for jar in "$ws_dir/lib/teavm"/*.jar; do
            if [ -f "$jar" ]; then
                classpath="$classpath:$jar"
            fi
        done
    fi
    
    # Compile
    javac -source 25 -target 25 -d "$bin_dir" -cp "$classpath" -encoding UTF-8 \
        $(find "$ws_dir/src/main/java" "$ws_dir/src/teavm/java" -name '*.java' -type f 2>/dev/null) \
        2>&1 || {
        error "Java compilation failed!"
        error "Make sure you have Java 25+ installed and the source files are valid."
        exit 1
    }
    
    success "Java compilation successful."
    
    # Step 2: Run TeaVM
    info "Step 2: Running TeaVM compilation..."
    
    # Check for TeaVM
    local teavm_jar=""
    if [ -d "$ws_dir/lib/teavm" ]; then
        teavm_jar=$(find "$ws_dir/lib/teavm" -name 'teavm-cli-*.jar' -type f 2>/dev/null | head -1)
    fi
    
    if [ -z "$teavm_jar" ]; then
        # Try Gradle cache
        teavm_jar=$(find "$HOME/.gradle/caches" -name 'teavm-cli-0.15.0.jar' -type f 2>/dev/null | head -1)
    fi
    
    if [ -n "$teavm_jar" ]; then
        local teavm_args="-Xmx2G"
        local teavm_mode=""
        
        case $mode in
            debug)
                teavm_mode="--debug-information"
                ;;
            release)
                teavm_mode="--optimization-level FULL --obfuscation-level FULL"
                ;;
            *)
                teavm_mode="--optimization-level SIMPLE"
                ;;
        esac
        
        java $teavm_args -cp "$classpath:$teavm_jar" \
            org.teavm.cli.TeaVMRunner \
            --target js \
            --main-class net.lax1dude.eaglercraft.v2_6.internal.teavm.MainClass \
            --output-dir "$out_dir" \
            $teavm_mode \
            2>&1 || {
            error "TeaVM compilation failed!"
            exit 1
        }
    else
        warn "TeaVM CLI JAR not found. Skipping TeaVM step."
        warn "Please install TeaVM 0.15.0 and place the JAR in workspace/lib/teavm/"
        info "You can download TeaVM from: https://teavm.org/"
    fi
    
    # Step 3: Copy JavaScript template
    if [ -d "$ws_dir/javascript" ]; then
        info "Step 3: Copying JavaScript template..."
        if [ -f "$SCRIPT_DIR/sources/setup/workspace_template/javascript/index.html" ]; then
            cp "$SCRIPT_DIR/sources/setup/workspace_template/javascript/index.html" \
                "$out_dir/index.html" 2>/dev/null || true
        fi
    fi
    
    # Calculate output size
    local total_size=$(du -sb "$out_dir" 2>/dev/null | cut -f1 || echo "0")
    local size_mb=$(echo "scale=1; $total_size / 1048576" | bc 2>/dev/null || echo "?")
    info "Output size: ${size_mb} MB"
    
    success "Fallback compilation complete."
}

main "$@"
