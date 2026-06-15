#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Initialize Workspace
# ============================================================================
# Decompile Minecraft 26.1.2 and apply EaglerCraft patches.
# 
# IMPORTANT: No MCP needed! MC 26.1.2 ships unobfuscated.
# This is a major simplification from the MC 1.8 version which required:
#   - Mod Coder Pack (MCP) download and extraction
#   - SpecialSource deobfuscation with SRG mappings
#   - MCInjector for generics/annotations (EXC mappings)
#   - Fernflower decompilation with CSV parameter mappings
#   - ECR patches without context lines
#
# Now we just:
#   1. Locate the unobfuscated JAR
#   2. Decompile with Vineflower (modern Fernflower fork)
#   3. Apply standard unified diff patches (with context!)
#
# Usage:
#   ./build_init.sh [options]
#
# Options:
#   --jar <path>         Path to minecraft-26.1.2.jar
#   --no-decompile       Skip decompilation step
#   --no-patches         Skip patch application
#   --vineflower-args    Additional Vineflower arguments
#   --help               Show this help message
# ============================================================================

set -e

# Determine script directory
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Print colored message
info()  { echo -e "${CYAN}[INIT]${NC} $1"; }
warn()  { echo -e "${YELLOW}[INIT]${NC} $1"; }
error() { echo -e "${RED}[INIT]${NC} $1"; }
success() { echo -e "${GREEN}[INIT]${NC} $1"; }

# Show help
show_help() {
    echo "EaglerCraft 26.1.2 - Initialize Workspace"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --jar <path>         Path to minecraft-26.1.2.jar"
    echo "  --no-decompile       Skip decompilation step"
    echo "  --no-patches         Skip patch application"
    echo "  --vineflower-args    Additional Vineflower arguments"
    echo "  --help               Show this help message"
    echo ""
    echo "This script will:"
    echo "  1. Locate Minecraft 26.1.2 JAR (unobfuscated!)"
    echo "  2. Extract and decompile with Vineflower"
    echo "  3. Apply EaglerCraft patches"
    echo "  4. Process assets from the JAR"
    echo ""
    echo "No MCP (Mod Coder Pack) is required since MC 26.1.2 is unobfuscated."
}

# Check prerequisites
check_prerequisites() {
    info "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        error "Java not found! Please install Java 25 or later."
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    info "Java version: $JAVA_VERSION"
    
    if [ "$JAVA_VERSION" -lt 21 ]; then
        warn "Java 21+ recommended for Vineflower decompilation"
    fi
    
    # Check for BuildTools JAR
    BUILD_TOOLS_JAR="$SCRIPT_DIR/buildtools/BuildTools.jar"
    if [ ! -f "$BUILD_TOOLS_JAR" ]; then
        warn "BuildTools.jar not found at: $BUILD_TOOLS_JAR"
        info "Attempting to build BuildTools..."
        
        if [ -f "$SCRIPT_DIR/buildtools/build.gradle" ]; then
            (cd "$SCRIPT_DIR/buildtools" && ./gradlew build 2>/dev/null) || {
                warn "Could not build BuildTools. Some features may be limited."
            }
        fi
    fi
    
    # Check for Vineflower
    VINEFLOWER_JAR="$SCRIPT_DIR/buildtools/lib/vineflower-1.10.1.jar"
    if [ ! -f "$VINEFLOWER_JAR" ]; then
        warn "Vineflower not found. It will be downloaded automatically during init."
    fi
    
    success "Prerequisites check complete."
}

# Main initialization
main() {
    # Parse arguments
    local jar_path=""
    local extra_args=""
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --jar)
                jar_path="$2"
                shift 2
                ;;
            --no-decompile|--no-patches)
                extra_args="$extra_args $1"
                shift
                ;;
            --vineflower-args)
                extra_args="$extra_args --vineflower-args $2"
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
    echo "║   EaglerCraft 26.1.2 - Workspace Initialization        ║"
    echo "║   No MCP Required - Unobfuscated JAR                    ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    # Check prerequisites
    check_prerequisites
    echo ""
    
    # Build jar argument if provided
    local jar_arg=""
    if [ -n "$jar_path" ]; then
        if [ ! -f "$jar_path" ]; then
            error "JAR file not found: $jar_path"
            exit 1
        fi
        jar_arg="--jar $jar_path"
        info "Using provided JAR: $jar_path"
    fi
    
    # Run the init task
    info "Starting initialization..."
    
    if [ -f "$BUILD_TOOLS_JAR" ]; then
        java -Xmx2G -jar "$BUILD_TOOLS_JAR" init $jar_arg $extra_args
    else
        # Fallback: run directly from source
        warn "BuildTools.jar not found, using fallback initialization..."
        fallback_init "$jar_path" "$extra_args"
    fi
    
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        echo ""
        success "Initialization complete!"
        info "Next steps:"
        info "  1. Run ./build_workspace.sh to create the development workspace"
        info "  2. Run ./build_compile.sh to compile the client"
    else
        error "Initialization failed with exit code $EXIT_CODE"
        exit $EXIT_CODE
    fi
}

# Fallback initialization without BuildTools.jar
fallback_init() {
    local jar_path="$1"
    local extra_args="$2"
    
    # If no JAR path provided, try to find it
    if [ -z "$jar_path" ]; then
        info "Searching for Minecraft 26.1.2 JAR..."
        
        # Check common locations
        local mc_dirs=(
            "$HOME/.minecraft/versions/26.1.2"
            "$HOME/Library/Application Support/minecraft/versions/26.1.2"
            "$APPDATA/.minecraft/versions/26.1.2"
        )
        
        for dir in "${mc_dirs[@]}"; do
            if [ -f "$dir/26.1.2.jar" ]; then
                jar_path="$dir/26.1.2.jar"
                success "Found: $jar_path"
                break
            fi
        done
        
        if [ -z "$jar_path" ]; then
            error "Could not locate Minecraft 26.1.2 JAR!"
            error "Please provide the path using --jar <path>"
            exit 1
        fi
    fi
    
    # Create output directories
    info "Creating output directories..."
    mkdir -p "$SCRIPT_DIR/sources/minecraft"
    mkdir -p "$SCRIPT_DIR/sources/minecraft-classes"
    mkdir -p "$SCRIPT_DIR/sources/resources"
    mkdir -p "$SCRIPT_DIR/sources/assets"
    
    # Extract classes from JAR
    info "Extracting classes from JAR..."
    (cd "$SCRIPT_DIR/sources/minecraft-classes" && jar xf "$jar_path")
    
    # Check for Vineflower
    if [ ! -f "$VINEFLOWER_JAR" ]; then
        info "Downloading Vineflower..."
        mkdir -p "$SCRIPT_DIR/buildtools/lib"
        curl -L -o "$VINEFLOWER_JAR" \
            "https://github.com/Vineflower/vineflower/releases/latest/download/vineflower-1.10.1.jar" \
            2>/dev/null || {
            error "Failed to download Vineflower!"
            error "Please download manually from: https://github.com/Vineflower/vineflower/releases"
            exit 1
        }
    fi
    
    # Decompile with Vineflower
    if [[ "$extra_args" != *"--no-decompile"* ]]; then
        info "Decompiling with Vineflower..."
        java -Xmx2G -jar "$VINEFLOWER_JAR" \
            --indent 4 \
            --remove-bridge true \
            --decompile-generics true \
            --overwrite \
            "$SCRIPT_DIR/sources/minecraft-classes" \
            "$SCRIPT_DIR/sources/minecraft" \
            2>&1 || warn "Some decompilation errors occurred (this is normal for large projects)"
    fi
    
    # Apply patches
    if [[ "$extra_args" != *"--no-patches"* ]]; then
        if [ -d "$SCRIPT_DIR/sources/patches" ]; then
            info "Applying patches..."
            (cd "$SCRIPT_DIR/sources/minecraft" && \
                for patch in $(find ../patches -name '*.patch' 2>/dev/null); do
                    patch -p1 --forward --no-backup-if-mismatch < "$patch" 2>/dev/null || \
                        warn "Patch failed: $(basename $patch)"
                done) || true
        fi
    fi
    
    # Write init marker
    echo "# EaglerCraft Init Marker" > "$SCRIPT_DIR/.eaglercraft-init"
    echo "version=26.1.2" >> "$SCRIPT_DIR/.eaglercraft-init"
    echo "initTime=$(date +%s)" >> "$SCRIPT_DIR/.eaglercraft-init"
    echo "jarPath=$jar_path" >> "$SCRIPT_DIR/.eaglercraft-init"
    
    success "Fallback initialization complete."
}

# Run main with all arguments
main "$@"
