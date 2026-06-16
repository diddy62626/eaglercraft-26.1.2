# Build Documentation

> Complete guide to building EaglercraftX 26.1.2 from source.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Step-by-Step Build Instructions](#step-by-step-build-instructions)
- [Build Configuration](#build-configuration)
- [Development Workspace Setup](#development-workspace-setup)
- [IDE Configuration](#ide-configuration)
- [Troubleshooting](#troubleshooting)
- [Advanced Build Options](#advanced-build-options)

---

## Prerequisites

### Required Software

| Software | Minimum Version | Recommended | Purpose |
|----------|----------------|-------------|---------|
| **JDK** | 25 | 25+ | Compile Java source + run Gradle |
| **Git** | 2.30+ | Latest | Version control |
| **Node.js** | 18+ | 20+ LTS | Asset pipeline scripts |
| **Python** | 3.8+ | 3.12+ | Build helper scripts |
| **Make** | GNU Make 4.0+ | Latest | Build orchestration (optional) |

### Required Files

| File | Purpose | Where to Get |
|------|---------|-------------|
| **Minecraft 26.1.2 JAR** | Base game source (unobfuscated) | Official Minecraft launcher |
| **Minecraft 26.1.2 assets** | Textures, sounds, lang files | Downloaded during build |

### System Requirements

| Resource | Minimum | Recommended |
|----------|---------|-------------|
| CPU | 4 cores | 8+ cores |
| RAM | 4 GB free | 8+ GB free |
| Disk | 2 GB free | 5+ GB free |
| Network | Required (downloads deps) | Broadband |

### Operating System Support

| OS | Status | Notes |
|----|--------|-------|
| Linux (x86_64) | ✅ Fully Supported | Primary development platform |
| macOS (ARM/x86) | ✅ Fully Supported | Tested on macOS 14+ |
| Windows 10/11 | ✅ Fully Supported | Use Git Bash or WSL2 |
| *BSD | ⚠️ Untested | Should work with proper JDK |

---

## Step-by-Step Build Instructions

### 1. Install JDK 25

**Linux (Ubuntu/Debian):**
```bash
# Add JDK 25 PPA
sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt update
sudo apt install openjdk-25-jdk

# Verify
java -version
# openjdk version "25" ...
```

**macOS:**
```bash
# Using Homebrew
brew install openjdk@25

# Set JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 25)' >> ~/.zshrc
source ~/.zshrc
```

**Windows:**
```powershell
# Using winget
winget install Microsoft.OpenJDK.25

# Or download from https://adoptium.net/
# Set JAVA_HOME environment variable to installation directory
```

### 2. Clone the Repository

```bash
git clone https://github.com/eaglercraft/eaglercraftx-26.git
cd eaglercraftx-26

# Verify repository integrity
git status
git log --oneline -5
```

### 3. Initialize the Build

```bash
chmod +x build_init.sh
./build_init.sh
```

The initialization script will prompt you for:

```
╔══════════════════════════════════════════════════════════╗
║         EaglercraftX 26.1.2 Build Initialization        ║
╠══════════════════════════════════════════════════════════╣
║                                                          ║
║  This script will set up the build environment.          ║
║  You need the Minecraft 26.1.2 vanilla JAR.             ║
║                                                          ║
║  Enter the path to your Minecraft 26.1.2 JAR:           ║
║  > /home/user/.minecraft/versions/26.1.2/26.1.2.jar    ║
║                                                          ║
║  ✔ JAR found and validated                              ║
║  ✔ Extracting unobfuscated classes...                    ║
║  ✔ Setting up TeaVM classpath...                         ║
║  ✔ Downloading dependencies...                           ║
║  ✔ Applying EaglerCraft patches...                       ║
║  ✔ Generating texture atlases...                         ║
║  ✔ Compressing audio assets...                           ║
║  ✔ Build environment ready!                              ║
║                                                          ║
╚══════════════════════════════════════════════════════════╝
```

**What `build_init.sh` does:**

1. **Validates** the Minecraft JAR (checks version string, hashes)
2. **Extracts** unobfuscated class files from the JAR
3. **Downloads** TeaVM compiler and runtime dependencies
4. **Downloads** LWJGL stub JARs (browser-compatible API stubs)
5. **Applies** EaglerCraft patches to the extracted source
6. **Generates** texture atlases from individual textures
7. **Compresses** audio files to OGG Vorbis
8. **Creates** the Gradle project structure

> **Important:** `build_init.sh` only needs to be run once. Subsequent builds use `build_compile.sh`.

### 4. Compile the Browser Client

```bash
chmod +x build_compile.sh
./build_compile.sh
```

**Compilation stages:**

```
[1/6] Compiling Java sources...          ████████████████████ 100%
[2/6] Running TeaVM compilation...       ████████████████████ 100%
       - Dependency analysis: 12,847 classes
       - Optimization (AGGRESSIVE): done
       - JavaScript generation: done
       - Output: 8.2 MB (minified)
[3/6] Bundling assets (EPK)...           ████████████████████ 100%
       - Textures: 1,247 files → 3 atlases
       - Sounds: 412 files (compressed)
       - Languages: 67 locales
       - Total EPK size: 18.4 MB
[4/6] Generating HTML shell...           ████████████████████ 100%
[5/6] Creating offline download...       ████████████████████ 100%
[6/6] Copying to output/...              ████████████████████ 100%

Build completed successfully!
Output directory: output/
  - index.html (2.1 KB)
  - eaglercraft.js (8.2 MB)
  - eaglercraft.js.map (24.6 MB)
  - assets.epk (18.4 MB)
  - offline_download.html (26.8 MB)
```

### 5. Test the Build

```bash
# Quick test with Python HTTP server
cd output
python3 -m http.server 8080

# Open in browser: http://localhost:8080
# Or use the system's default browser:
python3 -m webbrowser http://localhost:8080
```

### 6. Clean Build (if needed)

```bash
chmod +x build_clean.sh
./build_clean.sh

# This removes:
# - build/ (compiled class files)
# - output/ (generated output)
# - .gradle/ (Gradle cache)
# Does NOT remove:
# - sources/ (patched source code)
# - dependencies/ (downloaded JARs)
```

---

## Build Configuration

### Gradle Properties

Create or edit `gradle.properties` in the project root:

```properties
# Java version
javaVersion=25

# TeaVM configuration
teavm.optimizationLevel=AGGRESSIVE
teavm.minification=true
teavm.sourceMaps=true
teavm.maxHeap=4096

# Asset pipeline
atlas.maxSize=4096
atlas.mipmapLevels=4
audio.codec=ogg_vorbis
audio.quality=-q2

# Output
output.directory=output
output.offlineDownload=true
output.sourceMaps=true

# Parallel compilation
parallel.threads=4
```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `JAVA_HOME` | Auto-detected | JDK 25 installation path |
| `GRADLE_OPTS` | `-Xmx4g` | Gradle JVM options |
| `TEAVM_OPTS` | `-Xmx4g` | TeaVM compiler JVM options |
| `EAGLER_BUILD_THREADS` | `4` | Number of parallel build threads |
| `EAGLER_SKIP_ATLAS` | `false` | Skip atlas generation (faster incremental) |
| `EAGLER_SKIP_AUDIO` | `false` | Skip audio compression |
| `EAGLER_SKIP_TESTS` | `true` | Skip test execution |

---

## Development Workspace Setup

### Quick Setup

```bash
# 1. Clone and initialize (one-time)
git clone https://github.com/eaglercraft/eaglercraftx-26.git
cd eaglercraftx-26
./build_init.sh

# 2. Start development mode (watches for changes)
./gradlew develop

# This starts a file watcher that:
# - Recompiles changed Java files automatically
# - Re-runs TeaVM when class files change
# - Serves output/ on http://localhost:8080
# - Auto-refreshes the browser on changes
```

### Hot Reload

EaglerCraft supports limited hot reload during development:

| Change Type | Hot Reload? | How |
|-------------|-------------|-----|
| Java logic (non-rendering) | ✅ Yes | TeaVM incremental compile + page reload |
| Shader source | ✅ Yes | Runtime shader recompilation |
| Texture assets | ⚠️ Partial | Requires atlas regeneration + reload |
| Audio assets | ❌ No | Requires full rebuild |
| New Java classes | ❌ No | Requires full rebuild |
| Patches | ❌ No | Requires re-initialization |

---

## IDE Configuration

### IntelliJ IDEA

1. **Open the project:**
   - File → Open → Select the `eaglercraftx-26` directory
   - IntelliJ will detect the Gradle project and import it

2. **Configure JDK 25:**
   - File → Project Structure → Project → SDK → Add JDK → Select JDK 25 path
   - Set language level to "25 (Preview)"

3. **Install recommended plugins:**
   - TeaVM Support (for TeaVM-specific inspections)
   - .ignore (for .gitignore support)

4. **Run configurations:**
   - Add a new "Gradle" configuration
   - Name: "Build Client"
   - Gradle project: `eaglercraftx-26`
   - Tasks: `compileJava teavm`

5. **Code style:**
   - Settings → Editor → Code Style → Java
   - Set indentation to 4 spaces
   - Import the project's `.editorconfig` if available

### VS Code

1. **Install extensions:**
   ```
   Extension Pack for Java (Microsoft)
   Gradle for Java (Microsoft)
   EditorConfig (EditorConfig)
   ```

2. **Open the project:**
   ```bash
   code eaglercraftx-26
   ```

3. **Configure JDK:**
   - Add to `settings.json`:
   ```json
   {
       "java.configuration.runtimes": [
           {
               "name": "JavaSE-25",
               "path": "/usr/lib/jvm/java-25-openjdk",
               "default": true
           }
       ],
       "java.compile.nullAnalysis.mode": "automatic"
   }
   ```

4. **Build tasks:**
   - Add to `.vscode/tasks.json`:
   ```json
   {
       "version": "2.0.0",
       "tasks": [
           {
               "label": "Build Client",
               "type": "shell",
               "command": "./build_compile.sh",
               "group": "build"
           },
           {
               "label": "Clean Build",
               "type": "shell",
               "command": "./build_clean.sh",
               "group": "build"
           },
           {
               "label": "Dev Server",
               "type": "shell",
               "command": "./gradlew develop",
               "isBackground": true,
               "group": "build"
           }
       ]
   }
   ```

### Eclipse

1. **Import the project:**
   - File → Import → Gradle → Existing Gradle Project
   - Select the `eaglercraftx-26` directory

2. **Configure JDK 25:**
   - Window → Preferences → Java → Installed JREs → Add → Standard VM
   - Select JDK 25 installation directory
   - Set as default

3. **Set compiler compliance:**
   - Window → Preferences → Java → Compiler
   - Compiler compliance level: 25
   - Enable preview features

---

## Troubleshooting

### Build Fails: "Unsupported class file version"

**Error:**
```
error: unsupported class file version 69.0
```

**Cause:** You're using a JDK older than JDK 25.

**Solution:** Install JDK 25 and set `JAVA_HOME`:
```bash
export JAVA_HOME=/path/to/jdk-25
./build_compile.sh
```

### Build Fails: "TeaVM compilation error"

**Error:**
```
TeaVM compilation error: Method not found: java/lang/Object.newPreallocatedArray
```

**Cause:** TeaVM class library is missing or outdated.

**Solution:** Re-run `build_init.sh` to download the correct TeaVM dependencies:
```bash
./build_clean.sh
./build_init.sh
```

### Build Fails: "Out of memory"

**Error:**
```
java.lang.OutOfMemoryError: Java heap space
```

**Cause:** TeaVM compilation is memory-intensive.

**Solution:** Increase the heap size:
```bash
export GRADLE_OPTS="-Xmx8g"
export TEAVM_OPTS="-Xmx8g"
./build_compile.sh
```

### Build Fails: "Patch failed to apply"

**Error:**
```
ERROR: Patch patches/unified/minecraft/0001-WebGL2-adapter.patch failed to apply
```

**Cause:** The vanilla source has changed and the patch no longer matches.

**Solution:** 
1. Check if you have the correct MC 26.1.2 JAR
2. If the JAR is correct, the patch may need to be updated
3. Apply the patch manually and regenerate it:
```bash
# Skip the failing patch and apply the rest
./build_init.sh --skip-patch=0001-WebGL2-adapter.patch

# After manual fix, regenerate the patch
./gradlew generatePatches
```

### Blank Screen in Browser

**Cause:** Usually a JavaScript error in the compiled output.

**Solution:**
1. Open browser developer console (F12)
2. Look for error messages
3. Common causes:
   - Missing `eaglercraftXOpts` in HTML
   - Missing `assets.epk` file
   - CORS issues when serving from file://
4. Use an HTTP server instead of opening the HTML file directly

### Slow Compilation

**Cause:** TeaVM AGGRESSIVE optimization is slow.

**Solution:** Use a lower optimization level for development:
```properties
# In gradle.properties
teavm.optimizationLevel=FULL    # Faster compilation, larger output
# teavm.optimizationLevel=AGGRESSIVE  # Use for production builds only
```

---

## Advanced Build Options

### Building Only the Client

```bash
./gradlew :eaglercraft:teavm
```

### Building Only the Gateway Plugin

```bash
./gradlew :gateway:build
```

### Building with Custom Texture Atlas Size

```bash
./gradlew -Patlas.maxSize=8192 teavm
```

### Generating Source Maps

```bash
# Source maps are generated by default in debug builds
# Disable for production:
./gradlew -Pteavm.sourceMaps=false teavm
```

### Cross-Compilation

```bash
# Build for a specific browser target
./gradlew -Ptarget.browser=chrome teavm
./gradlew -Ptarget.browser=firefox teavm
./gradlew -Ptarget.browser=safari teavm
```

### Docker Build

```dockerfile
FROM eclipse-temurin:25-jdk

WORKDIR /build
COPY . .

RUN chmod +x build_init.sh build_compile.sh
RUN ./build_init.sh --non-interactive --jar=/build/minecraft-26.1.2.jar
RUN ./build_compile.sh

FROM nginx:alpine
COPY --from=0 /build/output/ /usr/share/nginx/html/
EXPOSE 80
```

```bash
docker build -t eaglercraftx-26 .
docker run -p 8080:80 eaglercraftx-26
```

### CI/CD Integration

**GitHub Actions example:**

```yaml
name: Build EaglercraftX
on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 25
        uses: actions/setup-java@v4
        with:
          java-version: '25'
          distribution: 'temurin'
      
      - name: Initialize build
        run: ./build_init.sh --non-interactive --jar=minecraft-26.1.2.jar
      
      - name: Compile
        run: ./build_compile.sh
      
      - name: Upload artifacts
        uses: actions/upload-artifact@v4
        with:
          name: eaglercraftx-output
          path: output/
```
