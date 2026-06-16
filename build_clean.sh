#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Clean Build Artifacts
# ============================================================================
# Removes all build artifacts, compiled output, and temporary files.
# Use --deep to also remove decompiled source and workspace.
#
# Usage:
#   ./build_clean.sh [options]
#
# Options:
#   --deep    Also remove decompiled source and workspace
#   --help    Show this help message
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { echo -e "${CYAN}[CLEAN]${NC} $1"; }
warn()  { echo -e "${YELLOW}[CLEAN]${NC} $1"; }
error() { echo -e "${RED}[CLEAN]${NC} $1"; }
success() { echo -e "${GREEN}[CLEAN]${NC} $1"; }

show_help() {
    echo "EaglerCraft 26.1.2 - Clean Build Artifacts"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --deep    Also remove decompiled source and workspace"
    echo "  --help    Show this help message"
    echo ""
    echo "Removes:"
    echo "  Normal:   Compiled output, cache, temporary files"
    echo "  Deep:     + Decompiled source, workspace directory"
}

main() {
    local deep=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --deep)
                deep=true
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
    echo "║   EaglerCraft 26.1.2 - Clean Build Artifacts             ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    local total_removed=0
    
    # Standard clean targets
    local clean_dirs=(
        "sources/minecraft-classes"
        "workspace/bin"
        "workspace/.teavm"
        "dist"
        "buildtools/out"
        "buildtools/tmp"
    )
    
    for dir in "${clean_dirs[@]}"; do
        local target="$SCRIPT_DIR/$dir"
        if [ -d "$target" ]; then
            local count=$(find "$target" -type f 2>/dev/null | wc -l)
            rm -rf "$target"
            total_removed=$((total_removed + count))
            info "Removed: $dir ($count files)"
        else
            info "Skipped: $dir (not found)"
        fi
    done
    
    # Clean .class files from sources
    local class_count=$(find "$SCRIPT_DIR/sources" -name '*.class' -type f 2>/dev/null | wc -l)
    if [ "$class_count" -gt 0 ]; then
        find "$SCRIPT_DIR/sources" -name '*.class' -type f -delete 2>/dev/null
        info "Removed $class_count .class files from sources/"
        total_removed=$((total_removed + class_count))
    fi
    
    # Clean TeaVM cache
    if [ -d "$SCRIPT_DIR/workspace/.teavm" ]; then
        local cache_count=$(find "$SCRIPT_DIR/workspace/.teavm" -type f 2>/dev/null | wc -l)
        rm -rf "$SCRIPT_DIR/workspace/.teavm"
        info "Removed TeaVM cache ($cache_count files)"
        total_removed=$((total_removed + cache_count))
    fi
    
    # Deep clean
    if [ "$deep" = true ]; then
        echo ""
        warn "Deep clean enabled - removing decompiled source and workspace..."
        
        local deep_dirs=(
            "sources/minecraft"
            "sources/minecraft-classes"
            "sources/resources"
            "sources/assets"
            "workspace"
        )
        
        for dir in "${deep_dirs[@]}"; do
            local target="$SCRIPT_DIR/$dir"
            if [ -d "$target" ]; then
                local count=$(find "$target" -type f 2>/dev/null | wc -l)
                rm -rf "$target"
                total_removed=$((total_removed + count))
                info "Deep removed: $dir ($count files)"
            fi
        done
        
        # Remove init marker
        if [ -f "$SCRIPT_DIR/.eaglercraft-init" ]; then
            rm -f "$SCRIPT_DIR/.eaglercraft-init"
            info "Removed init marker"
        fi
    fi
    
    echo ""
    success "Clean complete! Removed $total_removed files total."
    
    if [ "$deep" = true ]; then
        info "Run ./build_init.sh to re-initialize the workspace."
    fi
}

main "$@"
