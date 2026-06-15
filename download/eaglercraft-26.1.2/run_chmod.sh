#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Set Execute Permissions
# ============================================================================
# Sets execute permissions on all shell scripts in the project.
# Run this after cloning or extracting the project.
#
# Usage:
#   ./run_chmod.sh
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

info()    { echo -e "${CYAN}[CHMOD]${NC} $1"; }
success() { echo -e "${GREEN}[CHMOD]${NC} $1"; }

echo ""
echo "Setting execute permissions on shell scripts..."
echo ""

# Set permissions on main build scripts
SCRIPTS=(
    "build_init.sh"
    "build_workspace.sh"
    "build_compile.sh"
    "build_dist.sh"
    "build_clean.sh"
    "CompileLatestClient.sh"
    "run_chmod.sh"
)

for script in "${SCRIPTS[@]}"; do
    if [ -f "$SCRIPT_DIR/$script" ]; then
        chmod +x "$SCRIPT_DIR/$script"
        success "chmod +x $script"
    else
        info "Not found: $script (skipped)"
    fi
done

# Find and set permissions on any other .sh files
echo ""
info "Scanning for additional shell scripts..."

find "$SCRIPT_DIR" -name '*.sh' -type f 2>/dev/null | while read f; do
    if [ ! -x "$f" ]; then
        chmod +x "$f"
        success "chmod +x $(realpath --relative-to="$SCRIPT_DIR" "$f" 2>/dev/null || echo "$f")"
    fi
done

echo ""
success "All shell scripts now have execute permissions."
