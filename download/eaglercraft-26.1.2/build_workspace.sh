#!/bin/bash
# ============================================================================
# EaglerCraft 26.1.2 - Create Development Workspace
# ============================================================================
# Sets up the development workspace with IDE project files.
# Must be run after build_init.sh
#
# Usage:
#   ./build_workspace.sh [options]
#
# Options:
#   --no-eclipse    Skip Eclipse project files
#   --no-intellij   Skip IntelliJ project files
#   --no-vscode     Skip VS Code configuration
#   --no-ide        Skip all IDE project files
#   --help          Show this help message
# ============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

info()  { echo -e "${CYAN}[WORKSPACE]${NC} $1"; }
warn()  { echo -e "${YELLOW}[WORKSPACE]${NC} $1"; }
error() { echo -e "${RED}[WORKSPACE]${NC} $1"; }
success() { echo -e "${GREEN}[WORKSPACE]${NC} $1"; }

show_help() {
    echo "EaglerCraft 26.1.2 - Create Development Workspace"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Options:"
    echo "  --no-eclipse    Skip Eclipse project files"
    echo "  --no-intellij   Skip IntelliJ project files"
    echo "  --no-vscode     Skip VS Code configuration"
    echo "  --no-ide        Skip all IDE project files"
    echo "  --help          Show this help message"
    echo ""
    echo "Creates the development workspace structure:"
    echo "  workspace/"
    echo "    ├── src/main/java/     - EaglerCraft + MC source"
    echo "    ├── src/teavm/java/    - TeaVM-specific source"
    echo "    ├── resources/         - Assets and resources"
    echo "    ├── javascript/        - HTML/JS output"
    echo "    └── lib/               - Dependencies"
}

main() {
    local ide_args=""
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --no-eclipse|--no-intellij|--no-vscode|--no-ide)
                ide_args="$ide_args $1"
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
    echo "║   EaglerCraft 26.1.2 - Workspace Setup                  ║"
    echo "╚══════════════════════════════════════════════════════════╝"
    echo ""
    
    # Check init marker
    if [ ! -f "$SCRIPT_DIR/.eaglercraft-init" ]; then
        error "Init marker not found! Please run ./build_init.sh first."
        exit 1
    fi
    
    # Check Java
    if ! command -v java &> /dev/null; then
        error "Java not found! Please install Java 25 or later."
        exit 1
    fi
    
    BUILD_TOOLS_JAR="$SCRIPT_DIR/buildtools/BuildTools.jar"
    
    if [ -f "$BUILD_TOOLS_JAR" ]; then
        info "Running workspace setup via BuildTools..."
        java -Xmx1G -jar "$BUILD_TOOLS_JAR" workspace $ide_args
    else
        warn "BuildTools.jar not found, using fallback workspace setup..."
        fallback_workspace
    fi
    
    EXIT_CODE=$?
    
    if [ $EXIT_CODE -eq 0 ]; then
        echo ""
        success "Workspace setup complete!"
        info "Open the 'workspace/' directory in your IDE to start developing."
        info "Run ./build_compile.sh to compile the client."
    else
        error "Workspace setup failed with exit code $EXIT_CODE"
        exit $EXIT_CODE
    fi
}

fallback_workspace() {
    info "Creating workspace directories..."
    mkdir -p "$SCRIPT_DIR/workspace/src/main/java"
    mkdir -p "$SCRIPT_DIR/workspace/src/teavm/java"
    mkdir -p "$SCRIPT_DIR/workspace/resources/assets"
    mkdir -p "$SCRIPT_DIR/workspace/resources/epk"
    mkdir -p "$SCRIPT_DIR/workspace/javascript"
    mkdir -p "$SCRIPT_DIR/workspace/lib"
    mkdir -p "$SCRIPT_DIR/workspace/bin"
    
    # Copy EaglerCraft source
    if [ -d "$SCRIPT_DIR/sources/main/java" ]; then
        info "Copying EaglerCraft source..."
        cp -r "$SCRIPT_DIR/sources/main/java/"* "$SCRIPT_DIR/workspace/src/main/java/" 2>/dev/null || true
    fi
    
    # Copy TeaVM source
    if [ -d "$SCRIPT_DIR/sources/teavm/java" ]; then
        info "Copying TeaVM source..."
        cp -r "$SCRIPT_DIR/sources/teavm/java/"* "$SCRIPT_DIR/workspace/src/teavm/java/" 2>/dev/null || true
    fi
    
    # Copy Minecraft source
    if [ -d "$SCRIPT_DIR/sources/minecraft" ]; then
        info "Copying decompiled Minecraft source..."
        cp -r "$SCRIPT_DIR/sources/minecraft/"* "$SCRIPT_DIR/workspace/src/main/java/" 2>/dev/null || true
    fi
    
    # Copy resources
    if [ -d "$SCRIPT_DIR/sources/resources" ]; then
        info "Copying resources..."
        cp -r "$SCRIPT_DIR/sources/resources/"* "$SCRIPT_DIR/workspace/resources/" 2>/dev/null || true
    fi
    
    # Copy template files
    if [ -d "$SCRIPT_DIR/sources/setup/workspace_template/javascript" ]; then
        info "Copying JavaScript template..."
        cp -r "$SCRIPT_DIR/sources/setup/workspace_template/javascript/"* \
            "$SCRIPT_DIR/workspace/javascript/" 2>/dev/null || true
    fi
    
    # Create build.gradle
    info "Creating build configuration..."
    cat > "$SCRIPT_DIR/workspace/build.gradle" << 'GRADLE'
plugins {
    id 'java'
}

group = 'net.lax1dude.eaglercraft'
version = '2.6.0'

repositories {
    mavenCentral()
}

dependencies {
    implementation "org.teavm:teavm-classlib:0.15.0"
    implementation teavm.libs.jso
    implementation teavm.libs.jsoApis
}

sourceSets {
    main {
        java {
            srcDirs = ['src/main/java', 'src/teavm/java']
        }
        resources {
            srcDirs = ['resources']
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}
GRADLE
    
    # Create gradle.properties
    cat > "$SCRIPT_DIR/workspace/gradle.properties" << 'PROPS'
mcVersion=26.1.2
teavmVersion=0.15.0
projectGroup=net.lax1dude.eaglercraft
projectVersion=2.6.0
PROPS

    # Create Eclipse project files
    info "Creating IDE project files..."
    
    # .project
    cat > "$SCRIPT_DIR/workspace/.project" << 'ECLIPSE'
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
  <name>eaglercraft-26.1.2</name>
  <comment>EaglerCraft 26.1.2 Workspace</comment>
  <projects></projects>
  <buildSpec>
    <buildCommand>
      <name>org.eclipse.jdt.core.javabuilder</name>
    </buildCommand>
  </buildSpec>
  <natures>
    <nature>org.eclipse.jdt.core.javanature</nature>
  </natures>
</projectDescription>
ECLIPSE
    
    # IntelliJ module
    cat > "$SCRIPT_DIR/workspace/eaglercraft.iml" << 'IDEA'
<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager">
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/src/teavm/java" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/resources" type="java-resource" />
      <excludeFolder url="file://$MODULE_DIR$/bin" />
      <excludeFolder url="file://$MODULE_DIR$/.teavm" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>
IDEA

    # VS Code settings
    mkdir -p "$SCRIPT_DIR/workspace/.vscode"
    cat > "$SCRIPT_DIR/workspace/.vscode/settings.json" << 'VSCODE'
{
  "java.project.sourcePaths": ["src/main/java", "src/teavm/java"],
  "java.project.resourcePaths": ["resources"]
}
VSCODE

    success "Fallback workspace setup complete."
}

main "$@"
