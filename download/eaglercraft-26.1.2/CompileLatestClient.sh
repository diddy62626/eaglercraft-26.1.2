#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Full Build Pipeline
# ============================================================================
# Runs the complete build pipeline from init to distribution.
# This is the one-stop script to build everything from scratch.
#
# Pipeline:
#   1. Initialize workspace (decompile MC, apply patches)
#   2. Setup development workspace
#   3. Compile client via TeaVM
#   4. Create distribution packages
#
# Usage:
#   ./CompileLatestClient.sh [options]
#
# Options:
#   --skip-init          Skip initialization step
#   --skip-workspace     Skip workspace setup step
#   --release            Build release version
#   --debug              Build debug version
#   --jar <path>         Path to minecraft-26.1.2.jar
#   --clean-first        Clean before building
#   --help               Show this help message
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
MAGENTA='\033[0;35m'
NC='\033[0m'

info()    { echo -e "${CYAN}[BUILD]${NC} $1"; }
warn()    { echo -e "${YELLOW}[BUILD]${NC} $1"; }
error()   { echo -e "${RED}[BUILD]${NC} $1"; }
success() { echo -e "${GREEN}[BUILD]${NC} $1"; }
header()  { echo -e "${MAGENTA}[BUILD]${NC} $1"; }

show_help() {
    echo "EaglerCraft 26.1.2 - Full Build Pipeline"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --skip-init          Skip initialization step"
    echo "  --skip-workspace     Skip workspace setup step"
    echo "  --release            Build release version"
    echo "  --debug              Build debug version"
    echo "  --jar <path>         Path to minecraft-26.1.2.jar"
    echo "  --clean-first        Clean before building"
    echo "  --help               Show this help message"
    echo ""
    echo "Build Pipeline:"
    echo "  1. Initialize  - Decompile MC 26.1.2, apply patches"
    echo "  2. Workspace   - Setup development workspace"
    echo "  3. Compile     - TeaVM compilation to JavaScript"
    echo "  4. Distribute  - Create offline download packages"
}

# Timer for the entire build
BUILD_START_TIME=$(date +%s%N)

main() {
    local skip_init=false
    local skip_workspace=false
    local compile_mode=""
    local jar_path=""
    local clean_first=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-init)
                skip_init=true
                shift
                ;;
            --skip-workspace)
                skip_workspace=true
                shift
                ;;
            --release)
                compile_mode="--release"
                shift
                ;;
            --debug)
                compile_mode="--debug"
                shift
                ;;
            --jar)
                jar_path="$2"
                shift 2
                ;;
            --clean-first)
                clean_first=true
                shift
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
    echo "║   EaglerCraft 26.1.2 - Full Build Pipeline               ║"
    echo "║   From source to distribution in one step                ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    # Check Java
    if ! command -v java &> /dev/null; then
        error "Java not found! Please install Java 25 or later."
        exit 1
    fi
    
    info "Java version: $(java -version 2>&1 | head -1)"
    info "Build directory: $SCRIPT_DIR"
    echo ""
    
    # Step 0: Clean if requested
    if [ "$clean_first" = true ]; then
        header "═══ Step 0: Cleaning ═══"
        "$SCRIPT_DIR/build_clean.sh" --deep
        echo ""
    fi
    
    # Step 1: Initialize
    if [ "$skip_init" = false ]; then
        header "═══ Step 1: Initialize Workspace ═══"
        local init_args=""
        if [ -n "$jar_path" ]; then
            init_args="--jar $jar_path"
        fi
        
        if "$SCRIPT_DIR/build_init.sh" $init_args; then
            success "Step 1 complete: Initialization"
        else
            error "Step 1 FAILED: Initialization"
            exit 1
        fi
        echo ""
    else
        info "Step 1 SKIPPED: Initialization (--skip-init)"
        echo ""
    fi
    
    # Step 2: Setup workspace
    if [ "$skip_workspace" = false ]; then
        header "═══ Step 2: Setup Workspace ═══"
        
        if "$SCRIPT_DIR/build_workspace.sh"; then
            success "Step 2 complete: Workspace setup"
        else
            error "Step 2 FAILED: Workspace setup"
            exit 1
        fi
        echo ""
    else
        info "Step 2 SKIPPED: Workspace setup (--skip-workspace)"
        echo ""
    fi
    
    # Step 3: Compile
    header "═══ Step 3: Compile Client ═══"
    
    if "$SCRIPT_DIR/build_compile.sh" $compile_mode; then
        success "Step 3 complete: Compilation"
    else
        error "Step 3 FAILED: Compilation"
        exit 1
    fi
    echo ""
    
    # Step 4: Distribute
    header "═══ Step 4: Create Distribution ═══"
    
    if "$SCRIPT_DIR/build_dist.sh" --all; then
        success "Step 4 complete: Distribution"
    else
        error "Step 4 FAILED: Distribution"
        exit 1
    fi
    echo ""
    
    # Calculate build time
    BUILD_END_TIME=$(date +%s%N)
    BUILD_DURATION=$(( (BUILD_END_TIME - BUILD_START_TIME) / 1000000 ))
    BUILD_SECONDS=$((BUILD_DURATION / 1000))
    BUILD_MINUTES=$((BUILD_SECONDS / 60))
    
    echo "╔══════════════════════════════════════════════════════════╗"
    echo "║   Build Complete!                                        ║"
    if [ $BUILD_MINUTES -gt 0 ]; then
        echo "║   Total time: ${BUILD_MINUTES}m $((BUILD_SECONDS % 60))s                            ║"
    else
        echo "║   Total time: ${BUILD_SECONDS}s                                     ║"
    fi
    echo "║                                                          ║"
    echo "║   Output files:                                          ║"
    
    # List dist files
    if [ -d "$SCRIPT_DIR/dist" ]; then
        for f in "$SCRIPT_DIR/dist"/*; do
            if [ -f "$f" ]; then
                local fname=$(basename "$f")
                local fsize=$(du -h "$f" | cut -f1)
                echo "║     $fname ($fsize)                           ║"
            fi
        done
    fi
    
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    success "EaglerCraft 26.1.2 build complete!"
}

main "$@"
