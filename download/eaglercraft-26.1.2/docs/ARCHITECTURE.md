# Architecture Documentation

> EaglercraftX 26.1.2 — Internal Architecture Reference

This document describes the internal architecture of EaglercraftX 26.1.2, including the platform abstraction layer, compilation pipeline, rendering engine, networking stack, singleplayer engine, build system, and patch system.

---

## Table of Contents

- [Platform Abstraction Layer](#platform-abstraction-layer)
- [TeaVM Compilation Pipeline](#teavm-compilation-pipeline)
- [WebGL2 / WebGPU Rendering Pipeline](#webgl2--webgpu-rendering-pipeline)
- [Network Protocol Architecture](#network-protocol-architecture)
- [Singleplayer Web Worker Architecture](#singleplayer-web-worker-architecture)
- [Build System Design](#build-system-design)
- [Patch System](#patch-system)

---

## Platform Abstraction Layer

The Platform Abstraction Layer (PAL) is the core architectural pattern that makes EaglerCraft possible. It decouples Minecraft's code from platform-specific implementations by providing a consistent API surface that maps Java standard library and LWJGL calls to browser-native APIs.

### Design Principles

1. **Interface, not implementation** — All platform-specific code is accessed through interfaces. The Minecraft codebase only depends on interfaces, never on concrete platform classes.
2. **Compile-time substitution** — TeaVM's class rewriting replaces platform interface implementations with browser-specific code at compile time.
3. **Zero overhead** — Abstraction has no runtime cost. Interface calls are compiled to direct function calls by TeaVM.

### Core Interfaces

```
PlatformRuntime          — Application lifecycle, logging, thread management
PlatformInput            — Keyboard, mouse, touch, gamepad input
PlatformAudio            — Sound playback, music streaming, volume control
PlatformFilesystem       — File I/O via IndexedDB
PlatformWebRTC           — P2P networking (legacy, retained for compat)
PlatformOpenGL           — GL function dispatch (→ WebGL2 calls)
PlatformNetworking       — WebSocket transport layer
PlatformClipboard        — Clipboard read/write
PlatformScreenRecorder   — Screen capture and recording
```

### PlatformRuntime Lifecycle

```
┌─────────────┐     ┌─────────────┐     ┌──────────────┐
│  Bootstrap   │────►│  Initialize │────►│  Main Loop   │
│  (index.html)│     │  (TeaVM RT) │     │  (60fps tick) │
└─────────────┘     └─────────────┘     └──────┬───────┘
                                                │
                          ┌─────────────────────┤
                          │                     │
                    ┌─────▼──────┐      ┌───────▼───────┐
                    │  Game Tick │      │  Render Frame  │
                    │  (20 TPS)  │      │  (VSync/RAF)  │
                    └────────────┘      └───────────────┘
```

The `PlatformRuntime` class manages:
- **Memory management** — Provides `malloc`/`free`-style allocation on top of TeaVM's heap for native-interop data structures.
- **Thread simulation** — Java threads are mapped to Web Workers or emulated with cooperative multitasking on the main thread.
- **Timing** — `nanoTime()` and `currentTimeMillis()` map to `performance.now()`.
- **Application state** — Tracks whether the client is in loading, running, or crashed state.

### PlatformAudio Architecture

```
┌──────────────────────┐
│  Minecraft Sound     │
│  System (Ogg Vorbis) │
└──────────┬───────────┘
           │
┌──────────▼───────────┐     ┌──────────────────────┐
│  PlatformAudio       │────►│  Web Audio API       │
│  (Decode + Mix)      │     │  AudioContext        │
│                      │     │  GainNode (volume)   │
│  - SoundPool mgmt    │     │  PannerNode (3D)     │
│  - Channel allocation│     │  AnalyserNode (viz)  │
│  - Priority system   │     │  MediaStreamDest     │
└──────────────────────┘     └──────────────────────┘
```

Audio is decoded from OGG Vorbis at load time using a JavaScript decoder. Sound instances are created from decoded AudioBuffer objects and played through Web Audio API nodes. 3D positional audio uses `PannerNode` with HRTF panning when available.

---

## TeaVM Compilation Pipeline

TeaVM translates Java bytecode to JavaScript. EaglerCraft configures TeaVM for maximum optimization and minimal output size.

### Pipeline Stages

```
┌────────────────┐
│ Java Source     │  (Unobfuscated MC 26.1.2 + EaglerCraft patches)
│ + Dependencies  │  (LWJGL stubs, TeaVM classlib extensions)
└───────┬────────┘
        │ javac
        ▼
┌────────────────┐
│ Java Bytecode   │  (.class files)
└───────┬────────┘
        │ TeaVM frontend (class parsing, dependency analysis)
        ▼
┌────────────────┐
│ IR Graph        │  (TeaVM intermediate representation)
└───────┬────────┘
        │ TeaVM optimizer (AGGRESSIVE level)
        │ - Dead code elimination
        │ - Constant folding
        │ - Method inlining
        │ - Escape analysis
        │ - Loop optimization
        │ - Type narrowing
        ▼
┌────────────────┐
│ Optimized IR    │
└───────┬────────┘
        │ TeaVM backend (JavaScript rendering)
        ▼
┌────────────────┐
│ JavaScript      │  (eaglercraft.js)
│ + Source Map    │  (eaglercraft.js.map)
└────────────────┘
```

### TeaVM Configuration

Key `teavm.properties` settings:

```properties
# Optimization level — AGGRESSIVE enables all optimizations
# including risky ones like class merging and virtualization
teavm.optimization.level=AGGRESSIVE

# Class merging — reduces output size by merging similar classes
teavm.optimization.classMerging=true

# Virtualization — converts virtual calls to direct where possible
teavm.optimization.virtualization=true

# Dead code elimination — removes unreachable code
teavm.optimization.dce=true

# Debug information — generate source maps for development
teavm.debugInformation=true

# Minification — shorten variable and function names
teavm.minification=true

# Max heap for compilation (in MB)
teavm.maxHeap=4096
```

### Class Library Extensions

TeaVM provides a subset of the Java standard library, but EaglerCraft requires additional APIs. These are provided as stubs and implementations in the `teavm/classlib/` directory:

| Package | Provided By | Notes |
|---------|-------------|-------|
| `java.io` | TeaVM + EaglerCraft stubs | File I/O → IndexedDB |
| `java.nio` | EaglerCraft stubs | ByteBuffer, channels → JS ArrayBuffer |
| `java.nio.charset` | TeaVM | UTF-8 only |
| `java.net` | EaglerCraft stubs | URL, Socket → WebSocket |
| `java.util.concurrent` | TeaVM + stubs | Atomic, locks → JS Atomics |
| `java.util.zip` | EaglerCraft impl | Deflater/Inflater → pako.js |
| `javax.sound` | EaglerCraft impl | Sampled → Web Audio API |

---

## WebGL2 / WebGPU Rendering Pipeline

### Pipeline Overview

The rendering pipeline in EaglercraftX 26.1.2 is a fully deferred, physically-based renderer. This is a major departure from EaglerCraft 1.8, which used a simple forward renderer with fixed-function GL calls.

```
┌──────────────────────────────────────────────────────────┐
│                    Frame Rendering                        │
│                                                          │
│  ┌─────────┐    ┌──────────┐    ┌───────────────────┐   │
│  │  Shadow  │    │  G-Buffer│    │  Deferred Lighting│   │
│  │  Pass    │    │  Pass    │    │  Pass             │   │
│  │          │    │          │    │                   │   │
│  │ Depth +  │    │ Albedo + │    │ SSAO + SSR +     │   │
│  │ Shadow   │    │ Normal + │    │ Shadow + Direct  │   │
│  │ Map      │    │ PBR +   │    │ + Emissive =     │   │
│  │          │    │ Depth    │    │ HDR Lighting     │   │
│  └─────────┘    └──────────┘    └───────────────────┘   │
│                                          │               │
│  ┌───────────────────────────────────────▼─────────────┐ │
│  │                Post-Processing                      │ │
│  │                                                     │ │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐         │ │
│  │  │  Bloom   │  │Tonemap   │  │  FXAA    │────────►│ │
│  │  │  Extract │─►│(ACES)    │─►│          │  LDR    │ │
│  │  │  + Blur  │  │          │  │          │  Output │ │
│  │  └──────────┘  └──────────┘  └──────────┘         │ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────┘
```

### G-Buffer Layout

The G-Buffer uses multiple render targets (MRT) in a single geometry pass:

| Attachment | Format | Contents |
|------------|--------|----------|
| RT0 | RGBA8 | Albedo (RGB) + Metallic (A) |
| RT1 | RGBA8 | Normal (RGB) + Roughness (A) |
| RT2 | RGBA8 | Emissive (RGB) + AO (A) |
| RT3 | DEPTH24 | Scene depth |

### Chunk Rendering Pipeline

```
┌───────────────┐
│ Chunk Sections │  (16×16×16 block sections)
│ Rebuild Queue  │
└───────┬───────┘
        │
        ▼
┌───────────────┐     ┌────────────────┐
│ Mesh Builder   │────►│ Texture Atlas  │
│ (per-section)  │     │ Lookup         │
│                │     │ (mipmapped)    │
│ - Face culling │     └────────────────┘
│ - AO calc      │
│ - PBR material │
│ - Instancing   │
└───────┬───────┘
        │
        ▼
┌───────────────┐
│ VAO Cache      │  (VertexArrayObject per section)
│ + Buffer Pool  │  (Tiered allocation: 4KB, 16KB, 64KB, 256KB)
└───────┬───────┘
        │
        ▼
┌───────────────┐
│ Multi-Draw     │  (Batched draw calls per region)
│ Batching       │  Up to 64 sections per glMultiDrawElements
└───────┬───────┘
        │
        ▼
┌───────────────┐
│ G-Buffer       │  (Render to MRT FBO)
│ Shader         │
└───────────────┘
```

### Transform Feedback Particles

GPU-driven particles use WebGL2 transform feedback:

```
┌────────────────┐
│ Particle Emit   │  (CPU sets initial positions/velocities)
└───────┬────────┘
        │
        ▼
┌────────────────┐
│ Transform       │────► Updated positions/velocities
│ Feedback Pass   │      (written to GPU buffer, no CPU readback)
│ (vertex shader  │
│  updates state) │
└───────┬────────┘
        │
        ▼
┌────────────────┐
│ Particle Render │  (Point sprites or instanced quads)
└────────────────┘
```

### WebGPU Readiness

The platform abstraction for rendering is designed with WebGPU in mind:

- All state management goes through `EaglerAdapterGL30` interface
- The interface has been extended with WebGPU-compatible method signatures
- A future `EaglerAdapterWebGPU` implementation can be added without changing Minecraft code
- Resource handles are opaque integers that can map to either GL names or GPU pipeline objects

---

## Network Protocol Architecture

### Layer Stack

```
┌─────────────────────────────────────┐
│  Minecraft Protocol 775             │  (Packet serialization/deserialization)
│  - Handshake, Login, Play, Config   │
│  - Data Components for items        │
├─────────────────────────────────────┤
│  EaglerCraft Extension Protocol     │  (WebSocket-specific extensions)
│  - Skin/Cape upload                 │
│  - Voice signal                     │
│  - Server info ping                 │
├─────────────────────────────────────┤
│  WebSocket Transport                │  (Binary frames, permessage-deflate)
│  - Auto-reconnect                   │
│  - Rate limiting                    │
│  - Frame reassembly                 │
└─────────────────────────────────────┘
```

### Packet Flow

```
Client                                  Server
  │                                       │
  │──── Handshake (0x00) ────────────────►│  Protocol version, address
  │◄─── Status Response (0x00) ──────────│  Server MOTD, player count
  │──── Status Request (0x00) ──────────►│
  │                                       │
  │──── Login Start (0x00) ─────────────►│  Username, UUID
  │◄─── Login Success (0x02) ───────────│  UUID, username, properties
  │                                       │
  │◄─── Configuration Phase ────────────│  Registry, feature flags, etc.
  │──── Configuration Ack ──────────────►│
  │                                       │
  │◄─── Play Phase ─────────────────────│  Chunks, entities, etc.
  │──── Play Packets ──────────────────►│  Movement, actions, chat
```

### Compression

All packets are compressed using `permessage-deflate` at the WebSocket frame level, rather than the vanilla Minecraft zlib compression at the packet level. This provides:

- Better compression ratios (the deflate context is shared across frames)
- Lower per-packet overhead (no individual zlib headers)
- Hardware acceleration on some platforms

The `compressionThreshold` config option controls which packets are large enough to benefit from compression. Packets below the threshold are sent uncompressed to minimize latency.

---

## Singleplayer Web Worker Architecture

### Design

The singleplayer mode runs the Minecraft server in a dedicated Web Worker, communicating with the main thread (client) via SharedArrayBuffer for zero-copy data transfer.

```
┌──────────────────────────────────────────────────────────┐
│                    Main Thread (Client)                    │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │  Game Loop    │  │  Renderer    │  │  Input Handler │  │
│  │  (tick + render)│  │  (WebGL2)    │  │  (DOM events)  │  │
│  └──────┬───────┘  └──────┬───────┘  └───────┬───────┘  │
│         │                  │                   │          │
│  ┌──────┴──────────────────┴───────────────────┴───────┐  │
│  │               IPC Manager (Client Side)              │  │
│  │                                                      │  │
│  │  ┌─────────────┐  ┌───────────────────────────────┐  │  │
│  │  │ MessagePort  │  │ SharedArrayBuffer Regions     │  │  │
│  │  │ (control)    │  │ - Chunk data region (16MB)    │  │  │
│  │  │             │  │ - Entity update region (4MB)   │  │  │
│  │  │             │  │ - Event queue region (1MB)     │  │  │
│  │  └─────────────┘  └───────────────────────────────┘  │  │
│  └──────────────────────┬───────────────────────────────┘  │
└─────────────────────────┼──────────────────────────────────┘
                          │
              SharedArrayBuffer + MessagePort
                          │
┌─────────────────────────┼──────────────────────────────────┐
│                    Web Worker (Server)                       │
│                         │                                   │
│  ┌──────────────────────┴───────────────────────────────┐  │
│  │               IPC Manager (Server Side)               │  │
│  │  - Reads client input from shared memory              │  │
│  │  - Writes chunk updates to shared memory              │  │
│  │  - Posts control messages via MessagePort             │  │
│  └──────────────────────┬───────────────────────────────┘  │
│                          │                                  │
│  ┌───────────────────────▼───────────────────────────────┐ │
│  │            Integrated Minecraft Server                  │ │
│  │  - World generation                                    │ │
│  │  - Entity simulation                                   │ │
│  │  - Block ticking                                       │ │
│  │  - Game logic (20 TPS)                                 │ │
│  └───────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────┘
```

### Shared Memory Layout

The SharedArrayBuffer is divided into fixed regions:

| Region | Offset | Size | Purpose |
|--------|--------|------|---------|
| Chunk Data | 0 | 16 MB | Chunk sections, light data, biome data |
| Entity Updates | 16 MB | 4 MB | Entity positions, rotations, metadata |
| Event Queue | 20 MB | 1 MB | Ring buffer for game events (chat, deaths, etc.) |
| Client Input | 21 MB | 512 KB | Player movement, actions, inputs |
| Flags & Control | 21.5 MB | 512 KB | Synchronization flags, semaphores |

### Synchronization

Synchronization between the main thread and worker uses `Atomics` API:

- **Spin locks** for fast-path data access (chunk reads)
- **Atomics.wait/notify** for blocking operations (world load)
- **Double buffering** for frequently-updated regions (entity positions)

---

## Build System Design

### Build Flow

```
┌───────────────────┐
│ build_init.sh      │  (One-time setup)
│                   │
│ 1. Extract MC JAR │
│ 2. Download deps  │
│ 3. Apply patches  │
│ 4. Generate atlas │
│ 5. Compress audio │
│ 6. Setup classpath│
└────────┬──────────┘
         │
┌────────▼──────────┐
│ build_compile.sh   │  (Incremental builds)
│                   │
│ 1. Compile Java   │  → .class files
│ 2. TeaVM compile  │  → .js file
│ 3. Bundle assets  │  → .epk file
│ 4. Generate HTML  │  → index.html
│ 5. Copy to output/│
└───────────────────┘
```

### Gradle Project Layout

The build is organized as a multi-project Gradle build:

```groovy
// settings.gradle
include ':teavm'           // TeaVM runtime & classlib
include ':lwjgl-stubs'    // LWJGL API stubs
include ':minecraft'      // Patched MC source
include ':eaglercraft'    // EaglerCraft-specific code
include ':gateway'        // BungeeCord plugin
include ':velocity-plugin'// Velocity plugin
```

### Asset Pipeline

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Texture PNGs  │────►│ Atlas Packer │────►│ Texture Atlas│
│ (blocks,items)│     │ (max 4096²)  │     │ + Mipmaps    │
└──────────────┘     └──────────────┘     └──────┬───────┘
                                                  │
┌──────────────┐     ┌──────────────┐             │
│ Sound OGGs    │────►│ Audio Compress│            │
│               │     │ (quality -q2) │            │
└──────────────┘     └──────────────┘             │
                                                  │
┌──────────────┐     ┌──────────────┐             │
│ Lang JSONs    │────►│ Lang Pack    │             │
│               │     │              │             │
└──────────────┘     └──────────────┘             │
                                                  │
                              ┌───────────────────▼──┐
                              │ EPK Packager         │
                              │ (custom binary format)│
                              │ - LZMA2 compression  │
                              │ - Asset deduplication │
                              │ - CRC32 verification │
                              └──────────────────────┘
```

The EPK format is EaglerCraft's custom asset package format. It uses LZMA2 compression and includes CRC32 checksums for each entry. The format supports random access for streaming individual assets without decompressing the entire package.

---

## Patch System

### Overview

EaglerCraft maintains its modifications to the vanilla Minecraft source as patch files. This allows the project to track changes independently from the base game and makes updates to new MC versions manageable.

### Patch Formats

#### Unified Diff Format

Used for small, targeted changes:

```diff
--- a/net/minecraft/client/renderer/RenderSystem.java
+++ b/net/minecraft/client/renderer/RenderSystem.java
@@ -145,7 +145,7 @@
-    private static final boolean USE_WEBGL1 = true;
+    private static final boolean USE_WEBGL1 = false; // EaglerCraft: WebGL2 only
```

**When to use unified diff:**
- Small, localized changes (< 50 lines changed)
- Bug fixes and tweaks
- Changes that don't add new files

#### ECR (EaglerCraft Revision) Format

Used for large structural changes that would produce unwieldy diffs:

```
ECR/1.0
File: net/minecraft/client/Minecraft.java
Operation: REPLACE_METHOD
Target: initRendering()
---
// Complete replacement of the method body
private void initRendering() {
    this.renderer = new EaglerDeferredRenderer(this);
    // ... (full method implementation)
}
```

**When to use ECR:**
- Complete method replacements
- New class additions
- Large refactoring (> 50 lines)
- Changes that affect multiple interrelated methods

### Patch Application Order

Patches are applied in a deterministic order:

1. `patches/unified/minecraft/` — Sorted alphabetically by filename
2. `patches/unified/resources/` — Sorted alphabetically by filename
3. `patches/ecr/minecraft/` — Sorted by the order file
4. `patches/ecr/resources/` — Sorted by the order file

The `order` file in each ECR directory specifies the exact application order, which is important when patches depend on each other.

### Conflict Resolution

When a patch fails to apply:

1. The build system reports the failing patch with context
2. The developer must manually resolve the conflict
3. The updated patch is written back with the new base version
4. A `.orig` backup of the failed patch is saved for reference

### Patch Validation

The `scripts/validate_protocol.py` script can validate that:
- All patches apply cleanly
- No patch introduces syntax errors
- The patched source compiles without errors
- No patches are redundant (already applied to base)

---

## Error Handling Architecture

### Crash Reports

When an unrecoverable error occurs, EaglerCraft generates a crash report:

```
---- EaglercraftX Crash Report ----
// This is not a happy day.

Time: 2025-03-04 12:34:56
Description: Rendering entity in world

java.lang.NullPointerException: Cannot read field "position" because "entity" is null
    at net.minecraft.client.renderer.entity.EntityRenderer.render(EntityRenderer.java:145)
    at net.minecraft.client.renderer.entity.EntityRenderDispatcher.render(EntityRenderDispatcher.java:89)
    ...

EaglerCraft Details:
  Browser: Chrome 131.0.6778.86
  WebGL Version: 2.0
  GPU: NVIDIA GeForce RTX 4080
  Platform: Win32
  SharedArrayBuffer: Available
  Client Version: 2.6.0
  Protocol Version: 775
```

Crash reports are displayed in the browser and can be copied to clipboard or downloaded as a text file.
