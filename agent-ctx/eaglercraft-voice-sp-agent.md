# Task: EaglerCraft 26.1.2 Voice Chat & Singleplayer Systems

## Summary

Created all 7 Java files implementing the voice chat system and singleplayer (integrated server) systems for EaglerCraft 26.1.2.

## Files Created

### Part 1: Voice Chat System

1. **VoiceClient.java** (557 lines) - `sources/main/java/net/lax1dude/eaglercraft/v2_6/voice/`
   - Voice channel management (global, proximity, private)
   - Microphone capture via getUserMedia (native stubs)
   - Opus encoding/decoding support (native stubs)
   - Proximity-based 3D spatial audio with PannerNode
   - Mute/deafen system with per-player controls
   - Push-to-talk and voice activation (VAD) modes
   - Volume controls per-player (0.0-2.0 range)
   - Speaking indicator and peer timeout detection
   - Tick-based update loop for VAD and peer health

2. **VoiceSignalPackets.java** (442 lines) - `sources/main/java/net/lax1dude/eaglercraft/v2_6/voice/`
   - 8 signal packet types (connect, disconnect, ICE, SDP offer/answer, mute, channel change, global)
   - Binary packet format: [1 byte type][variable payload]
   - Modified UTF-8 string encoding with 2-byte length prefix
   - SimpleDataInputStream inner class for efficient payload parsing
   - Outgoing signal construction with ByteArrayOutputStream
   - Incoming signal dispatch to VoiceClient handlers
   - Signal sender callback interface for network layer integration

### Part 2: Singleplayer / Integrated Server

3. **SPClient.java** (490 lines) - `sources/main/java/net/lax1dude/eaglercraft/v2_6/sp/`
   - Web Worker lifecycle management (CREATED→STARTING→RUNNING→PAUSED→STOPPING→STOPPED)
   - Player input dispatch (position, rotation, on-ground state)
   - Block change and chat message forwarding to worker
   - World save/load coordination
   - Pause/resume handling for game menu
   - LAN relay integration with 5-char join code generation
   - Pending message queue for pre-worker-ready messages
   - SPClientListener callback interface for game events

4. **IPCOutputStream.java** (322 lines) - `sources/main/java/net/lax1dude/eaglercraft/v2_6/sp/`
   - Fast IPC data output with pre-sized buffers
   - All primitive types: byte, boolean, short, int, long, float, double
   - VarInt/VarLong encoding (Minecraft protocol format)
   - String and byte array writes with VarInt length prefix
   - Growable buffer with 1.5x expansion
   - ArrayBuffer conversion for Web Worker transfer
   - Zero-copy transferArrayBuffer() method
   - Static varIntSize() utility for buffer pre-sizing

5. **IPCInputStream.java** (361 lines) - `sources/main/java/net/lax1dude/eaglercraft/v2_6/sp/`
   - Fast IPC data input from byte arrays
   - All primitive types with big-endian byte order
   - VarInt/VarLong decoding with overflow protection
   - String and byte array reads with VarInt length prefix
   - readFully() for bulk chunk data reads
   - readRemaining() for variable-length payloads
   - Bounds checking with clear NoSuchElementException
   - Position tracking and skip/reset support

### Part 3: Singleplayer Web Worker Implementation

6. **WorkerMain.java** (329 lines) - `sources/teavm/java/net/lax1dude/eaglercraft/v2_6/internal/sp/`
   - Web Worker entry point with TeaVM @JSBody annotations
   - Fixed-timestep server loop (20 TPS, max 4 ticks/frame)
   - SharedArrayBuffer probing and creation (16MB)
   - IPC message dispatch (12 channel types client→worker, 9 worker→client)
   - ArrayBuffer transfer with zero-copy via postMessage
   - IntegratedServer lifecycle management
   - Error propagation to main thread
   - Spiral-of-death prevention for tick accumulator

7. **IntegratedServer.java** (679 lines) - `sources/teavm/java/net/lax1dude/eaglercraft/v2_6/internal/sp/`
   - Complete integrated server with tick pipeline
   - Chunk generation with seed-based hash noise terrain
   - Chunk population stubs (trees, ores, etc.)
   - 16x256x16 chunk format with block IDs, metadata, lighting
   - View distance-based chunk loading/unloading
   - Entity tracking and tick system
   - Scheduled block update processing
   - IndexedDB persistence with @JSBody native methods
   - Autosave every 600 ticks (30 seconds)
   - World metadata save/load (seed, time, game mode)
   - Player spawn handling with initial chunk generation
   - All IPC message handlers (input, block change, chat, save/load, pause/resume)

8. **EaglerWorker.java** (376 lines) - `sources/teavm/java/net/lax1dude/eaglercraft/v2_6/internal/sp/`
   - Base Web Worker abstraction with @JSBody annotations
   - DedicatedWorkerGlobalScope wrapper
   - MessageHandler/ErrorHandler JS functor interfaces
   - ArrayBuffer transfer with postMessage [data] transfer list
   - Worker creation from blob URL or same-origin path
   - Error propagation to main thread via special packet format
   - SharedArrayBuffer support with Int32Array view creation
   - Conversion utilities: bytes↔ArrayBuffer
   - Clean terminate() with resource cleanup

## Architecture Patterns

- **main/java** = Platform-independent game logic with native method stubs
- **teavm/java** = Browser-specific implementations with @JSBody annotations
- IPC protocol: [1 byte channel][variable payload] over ArrayBuffer transfer
- TeaVM callback pattern: `__methodName` called from JS via mangled class name
- Worker communication: `self.postMessage(data, [data])` for zero-copy transfer
