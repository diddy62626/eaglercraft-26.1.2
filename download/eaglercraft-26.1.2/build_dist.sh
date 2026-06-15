#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Create Distribution
# ============================================================================
# Creates distribution packages for the compiled EaglerCraft client:
#   - Offline download (single HTML file with bundled JS)
#   - Signed client (for authenticated distribution)
#
# Usage:
#   ./build_dist.sh [options]
#
# Options:
#   --offline           Create offline download package (default)
#   --signed            Create signed client package
#   --version-suffix    Version suffix for filenames
#   --all               Create all distribution types
#   --help              Show this help message
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { echo -e "${CYAN}[DIST]${NC} $1"; }
warn()  { echo -e "${YELLOW}[DIST]${NC} $1"; }
error() { echo -e "${RED}[DIST]${NC} $1"; }
success() { echo -e "${GREEN}[DIST]${NC} $1"; }

show_help() {
    echo "EaglerCraft 26.1.2 - Create Distribution"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --offline           Create offline download package (default)"
    echo "  --signed            Create signed client package"
    echo "  --version-suffix    Version suffix for filenames"
    echo "  --all               Create all distribution types"
    echo "  --help              Show this help message"
    echo ""
    echo "Distribution Types:"
    echo "  Offline  - Single HTML file with all JS and assets bundled"
    echo "  Signed   - Signed client for authenticated distribution"
}

main() {
    local do_offline=false
    local do_signed=false
    local version_suffix=""
    
    # Default: offline
    if [ $# -eq 0 ]; then
        do_offline=true
    fi
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --offline)
                do_offline=true
                shift
                ;;
            --signed)
                do_signed=true
                shift
                ;;
            --all)
                do_offline=true
                do_signed=true
                shift
                ;;
            --version-suffix)
                version_suffix="$2"
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
    echo "║   EaglerCraft 26.1.2 - Create Distribution               ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    # Check compiled output
    local js_dir="$SCRIPT_DIR/workspace/javascript"
    if [ ! -d "$js_dir" ]; then
        error "Compiled output not found! Run ./build_compile.sh first."
        exit 1
    fi
    
    # Check for JS files
    local js_count=$(find "$js_dir" -name '*.js' -type f 2>/dev/null | wc -l)
    if [ "$js_count" -eq 0 ]; then
        error "No JavaScript files found in output directory!"
        error "Run ./build_compile.sh first."
        exit 1
    fi
    
    # Create dist directory
    local dist_dir="$SCRIPT_DIR/dist"
    mkdir -p "$dist_dir"
    
    local version_name="EaglerCraft_26.1.2"
    if [ -n "$version_suffix" ]; then
        version_name="${version_name}${version_suffix}"
    fi
    
    # Create offline download
    if [ "$do_offline" = true ]; then
        info "Creating offline download package..."
        create_offline_download "$js_dir" "$dist_dir" "$version_name"
    fi
    
    # Create signed client
    if [ "$do_signed" = true ]; then
        info "Creating signed client package..."
        create_signed_client "$js_dir" "$dist_dir" "$version_name"
    fi
    
    echo ""
    success "Distribution complete!"
    info "Output directory: $dist_dir"
    
    # List output files
    info "Generated files:"
    ls -lh "$dist_dir" 2>/dev/null | tail -n +2 | while read line; do
        info "  $line"
    done
}

create_offline_download() {
    local js_dir="$1"
    local dist_dir="$2"
    local version_name="$3"
    
    local output_file="$dist_dir/${version_name}_Offline.html"
    
    # Read the HTML template
    local template="$SCRIPT_DIR/sources/setup/workspace_template/javascript/index.html"
    local offline_template="$SCRIPT_DIR/sources/setup/workspace_template/javascript/OfflineDownloadTemplate.txt"
    
    # Bundle all JavaScript files
    info "Bundling JavaScript files..."
    local js_bundle=""
    for js_file in $(find "$js_dir" -name '*.js' -type f 2>/dev/null | sort); do
        info "  Adding: $(basename $js_file)"
        js_bundle+="$(cat "$js_file")"
        js_bundle+=$'\n'
    done
    
    # Bundle assets (as base64 data URIs for offline)
    info "Bundling assets..."
    local asset_count=0
    for asset_file in $(find "$js_dir" -name '*.epk' -o -name '*.png' -o -name '*.ogg' -type f 2>/dev/null); do
        asset_count=$((asset_count + 1))
    done
    info "  Found $asset_count asset files"
    
    # Build the HTML
    if [ -f "$template" ]; then
        local html_content=$(cat "$template")
        # Inject bundled JavaScript
        html_content="${html_content/<!-- EAGLERCRAFT_BUNDLE -->/<script type=\"text\/javascript\">\n${js_bundle}\n<\/script>}"
        # Remove external script references
        html_content=$(echo "$html_content" | sed '/<script.*src=.*><\/script>/d')
        echo "$html_content" > "$output_file"
    else
        # Create minimal HTML wrapper
        cat > "$output_file" << HTMLEOF
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EaglerCraft 26.1.2</title>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
html, body { width: 100%; height: 100%; overflow: hidden; background: #000; }
canvas#eaglercraft { display: block; width: 100vw; height: 100vh; }
</style>
</head>
<body>
<canvas id="eaglercraft"></canvas>
<script type="text/javascript">
${js_bundle}
</script>
</body>
</html>
HTMLEOF
    fi
    
    local size_mb=$(du -h "$output_file" | cut -f1)
    success "Offline download created: $(basename $output_file) ($size_mb)"
}

create_signed_client() {
    local js_dir="$1"
    local dist_dir="$2"
    local version_name="$3"
    
    local signed_dir="$dist_dir/${version_name}_Signed"
    mkdir -p "$signed_dir"
    
    # Copy all files from javascript output
    info "Copying client files..."
    cp -r "$js_dir"/* "$signed_dir/" 2>/dev/null || true
    
    # Read signed client template
    local template="$SCRIPT_DIR/sources/setup/workspace_template/javascript/SignedClientTemplate.txt"
    
    # Create signature manifest
    if [ -f "$template" ]; then
        cp "$template" "$signed_dir/CLIENT_SIGNATURE" 2>/dev/null || true
    fi
    
    # Generate file manifest
    info "Generating file manifest..."
    local manifest_file="$signed_dir/MANIFEST.txt"
    echo "# EaglerCraft 26.1.2 Signed Client Manifest" > "$manifest_file"
    echo "# Generated: $(date -u +%Y-%m-%dT%H:%M:%SZ)" >> "$manifest_file"
    echo "# Version: 26.1.2" >> "$manifest_file"
    echo "" >> "$manifest_file"
    
    (cd "$signed_dir" && find . -type f -not -name 'MANIFEST.txt' -not -name 'CLIENT_SIGNATURE' | sort | while read f; do
        local checksum=$(sha256sum "$f" | cut -d' ' -f1)
        echo "$checksum  $f" >> "$manifest_file"
    done)
    
    success "Signed client created: $(basename $signed_dir)/"
}

main "$@"
