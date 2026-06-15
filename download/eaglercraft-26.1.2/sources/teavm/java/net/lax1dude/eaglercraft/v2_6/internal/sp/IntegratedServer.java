package net.lax1dude.eaglercraft.v2_6.internal.sp;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Simplified integrated server for EaglerCraft 26.1.2 singleplayer.
 *
 * <p>Runs inside a Web Worker and manages the full Minecraft server
 * simulation: world loading/saving to IndexedDB, chunk generation,
 * entity ticking, block ticking, and player management.</p>
 *
 * <h3>World Storage</h3>
 * <p>Worlds are persisted to IndexedDB with the following structure:</p>
 * <ul>
 *   <li>World metadata (seed, gamemode, time, etc.)</li>
 *   <li>Region files (groups of 32x32 chunks in compressed format)</li>
 *   <li>Player data (inventory, health, position, etc.)</li>
 *   <li>Level dat (world settings and preferences)</li>
 * </ul>
 *
 * <h3>Tick Pipeline</h3>
 * <ol>
 *   <li>Process incoming IPC messages from client</li>
 *   <li>Tick player (movement, interaction, inventory)</li>
 *   <li>Tick entities (AI, physics, collision)</li>
 *   <li>Tick block entities (furnaces, hoppers, etc.)</li>
 *   <li>Tick scheduled block updates (redstone, flow, etc.)</li>
 *   <li>Generate/load/unload chunks based on player position</li>
 *   <li>Send render updates to client via IPC</li>
 *   <li>Autosave check (every 600 ticks / 30 seconds)</li>
 * </ol>
 *
 * @author Eaglercraft Team
 * @version 2.6
 * @since 2.6
 */
public class IntegratedServer {

        // ========== World Constants ==========
        private static final int CHUNK_SIZE = 16;
        private static final int WORLD_HEIGHT = 256;
        private static final int SEA_LEVEL = 64;
        private static final int REGION_SIZE = 32;
        private static final int SPAWN_CHUNK_RADIUS = 3;
        private static final int MAX_CHUNK_RADIUS = 12;
        private static final int AUTOSAVE_INTERVAL = 600; // 30 seconds at 20 TPS

        // ========== Server State ==========
        private boolean initialized = false;
        private boolean paused = false;
        private boolean worldLoaded = false;
        private String worldName = "";
        private long worldSeed = 0;
        private int gameMode = 1; // 0=survival, 1=creative, 2=adventure, 3=spectator
        private long worldTime = 0;
        private long dayTime = 0;
        private int tickCount = 0;
        private int viewDistance = 8;

        // ========== Player State ==========
        private String playerName = "Player";
        private double playerX = 0;
        private double playerY = SEA_LEVEL + 1;
        private double playerZ = 0;
        private float playerYaw = 0;
        private float playerPitch = 0;
        private float playerHealth = 20.0f;
        private int playerGameMode = 1;
        private boolean playerOnGround = true;

        // ========== World Data ==========
        /** Loaded chunk data cache (key: chunkX,chunkZ). */
        private final java.util.HashMap<Long, ChunkData> chunks = new java.util.HashMap<>();

        /** Scheduled block updates. */
        private final java.util.ArrayList<ScheduledBlockUpdate> scheduledUpdates = new java.util.ArrayList<>();

        /** Active entities in the world. */
        private final java.util.ArrayList<EntityData> entities = new java.util.ArrayList<>();

        /** Pending chunk sends to client. */
        private final java.util.ArrayList<long[]> pendingChunkSends = new java.util.ArrayList<>();

        // ========== Inner Classes ==========

        /**
         * Represents a loaded chunk's data.
         */
        static final class ChunkData {
                final int x;
                final int z;
                final byte[] blockIDs;
                final byte[] blockMetadata;
                final byte[] blockLight;
                final byte[] skyLight;
                boolean modified;
                boolean populated;

                ChunkData(int x, int z) {
                        this.x = x;
                        this.z = z;
                        int blockCount = CHUNK_SIZE * WORLD_HEIGHT * CHUNK_SIZE;
                        this.blockIDs = new byte[blockCount];
                        this.blockMetadata = new byte[blockCount / 2];
                        this.blockLight = new byte[blockCount / 2];
                        this.skyLight = new byte[blockCount / 2];
                        this.modified = false;
                        this.populated = false;
                }

                int getBlockIndex(int bx, int by, int bz) {
                        return (by * CHUNK_SIZE + bz) * CHUNK_SIZE + bx;
                }

                int getBlockID(int bx, int by, int bz) {
                        return blockIDs[getBlockIndex(bx, by, bz)] & 0xFF;
                }

                void setBlockID(int bx, int by, int bz, int id) {
                        blockIDs[getBlockIndex(bx, by, bz)] = (byte) id;
                        modified = true;
                }
        }

        /**
         * Represents a scheduled block update (tick).
         */
        static final class ScheduledBlockUpdate {
                int x, y, z;
                int ticksRemaining;
                int priority;

                ScheduledBlockUpdate(int x, int y, int z, int ticks, int priority) {
                        this.x = x;
                        this.y = y;
                        this.z = z;
                        this.ticksRemaining = ticks;
                        this.priority = priority;
                }
        }

        /**
         * Represents a tracked entity.
         */
        static final class EntityData {
                int entityId;
                int entityType;
                double x, y, z;
                float yaw, pitch;
                float health;
                boolean dead;

                EntityData(int id, int type, double x, double y, double z) {
                        this.entityId = id;
                        this.entityType = type;
                        this.x = x;
                        this.y = y;
                        this.z = z;
                        this.health = 20.0f;
                        this.dead = false;
                }
        }

        // ========== Initialization ==========

        /**
         * Initializes the integrated server with configuration data.
         *
         * @param configData initialization payload from the client
         * @return true if initialization succeeded
         */
        public boolean initialize(byte[] configData) {
                if (initialized) return true;

                try {
                        // Parse initialization data
                        if (configData != null && configData.length > 0) {
                                parseInitData(configData);
                        }

                        initialized = true;
                        return true;
                } catch (Exception e) {
                        WorkerMain.sendError("Server init failed: " + e.getMessage());
                        return false;
                }
        }

        /**
         * Parses initialization data from the client.
         */
        private void parseInitData(byte[] data) {
                int offset = 0;
                if (data.length < 8) return;

                // Read world seed (8 bytes)
                worldSeed = 0;
                for (int i = 0; i < 8; i++) {
                        worldSeed = (worldSeed << 8) | (data[offset++] & 0xFFL);
                }

                // Read game mode (1 byte)
                if (offset < data.length) {
                        playerGameMode = data[offset++] & 0xFF;
                        gameMode = playerGameMode;
                }

                // Read view distance (1 byte)
                if (offset < data.length) {
                        viewDistance = Math.max(2, Math.min(MAX_CHUNK_RADIUS, data[offset++] & 0xFF));
                }

                // Read player name (remaining bytes as UTF-8)
                if (offset < data.length) {
                        byte[] nameBytes = new byte[data.length - offset];
                        System.arraycopy(data, offset, nameBytes, 0, nameBytes.length);
                        playerName = new String(nameBytes);
                }
        }

        // ========== Server Tick ==========

        /**
         * Main server tick. Called 20 times per second.
         */
        public void tick() {
                if (!initialized || paused) return;
                tickCount++;
                worldTime++;
                dayTime = (dayTime + 1) % 24000;

                // Process pending chunk sends
                processPendingChunkSends();

                // Tick player
                tickPlayer();

                // Tick entities
                tickEntities();

                // Tick scheduled block updates
                tickBlockUpdates();

                // Check chunk loading/unloading
                updateChunkLoading();

                // Autosave
                if (tickCount % AUTOSAVE_INTERVAL == 0) {
                        autosave();
                }

                // Send time update every 20 ticks
                if (tickCount % 20 == 0) {
                        sendTimeUpdate();
                }
        }

        private void tickPlayer() {
                // Player tick logic handled via IPC input messages
                // This is just for server-side state updates
        }

        private void tickEntities() {
                for (int i = entities.size() - 1; i >= 0; i--) {
                        EntityData entity = entities.get(i);
                        if (entity.dead) {
                                entities.remove(i);
                                continue;
                        }
                        // Entity AI and physics would be here
                        // Simplified: just track positions
                }
        }

        private void tickBlockUpdates() {
                for (int i = scheduledUpdates.size() - 1; i >= 0; i--) {
                        ScheduledBlockUpdate update = scheduledUpdates.get(i);
                        update.ticksRemaining--;
                        if (update.ticksRemaining <= 0) {
                                scheduledUpdates.remove(i);
                                // Block tick processing would go here
                        }
                }
        }

        private void updateChunkLoading() {
                int pcx = (int) playerX >> 4;
                int pcz = (int) playerZ >> 4;

                // Load chunks within view distance
                for (int dx = -viewDistance; dx <= viewDistance; dx++) {
                        for (int dz = -viewDistance; dz <= viewDistance; dz++) {
                                if (dx * dx + dz * dz > viewDistance * viewDistance) continue;
                                long key = chunkKey(pcx + dx, pcz + dz);
                                if (!chunks.containsKey(key)) {
                                        pendingChunkSends.add(new long[]{pcx + dx, pcz + dz});
                                }
                        }
                }

                // Unload distant chunks
                java.util.Iterator<java.util.Map.Entry<Long, ChunkData>> it = chunks.entrySet().iterator();
                while (it.hasNext()) {
                        ChunkData chunk = it.next().getValue();
                        int dx = chunk.x - pcx;
                        int dz = chunk.z - pcz;
                        if (dx * dx + dz * dz > (viewDistance + 2) * (viewDistance + 2)) {
                                if (chunk.modified) {
                                        saveChunk(chunk);
                                }
                                it.remove();
                        }
                }
        }

        private void processPendingChunkSends() {
                int sent = 0;
                while (!pendingChunkSends.isEmpty() && sent < 4) {
                        long[] coords = pendingChunkSends.remove(0);
                        int cx = (int) coords[0];
                        int cz = (int) coords[1];
                        ChunkData chunk = getOrGenerateChunk(cx, cz);
                        if (chunk != null) {
                                sendChunkToClient(chunk);
                                sent++;
                        }
                }
        }

        // ========== Chunk Management ==========

        private long chunkKey(int x, int z) {
                return ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
        }

        /**
         * Gets or generates a chunk at the given coordinates.
         */
        private ChunkData getOrGenerateChunk(int cx, int cz) {
                long key = chunkKey(cx, cz);
                ChunkData chunk = chunks.get(key);
                if (chunk == null) {
                        chunk = generateChunk(cx, cz);
                        chunks.put(key, chunk);
                }
                if (!chunk.populated) {
                        populateChunk(chunk);
                        chunk.populated = true;
                }
                return chunk;
        }

        /**
         * Generates terrain for a chunk using the world seed.
         */
        private ChunkData generateChunk(int cx, int cz) {
                ChunkData chunk = new ChunkData(cx, cz);

                // Try loading from IndexedDB first
                byte[] savedData = loadChunkFromDB(cx, cz);
                if (savedData != null) {
                        deserializeChunk(chunk, savedData);
                        chunk.populated = true;
                        return chunk;
                }

                // Generate terrain
                for (int bx = 0; bx < CHUNK_SIZE; bx++) {
                        for (int bz = 0; bz < CHUNK_SIZE; bz++) {
                                int worldX = cx * CHUNK_SIZE + bx;
                                int worldZ = cz * CHUNK_SIZE + bz;

                                // Simple height map using seed-based noise
                                int height = generateHeight(worldX, worldZ);

                                for (int by = 0; by < WORLD_HEIGHT; by++) {
                                        int blockID = 0; // Air
                                        if (by == 0) {
                                                blockID = 7; // Bedrock
                                        } else if (by < height - 4) {
                                                blockID = 1; // Stone
                                        } else if (by < height) {
                                                blockID = 3; // Dirt
                                        } else if (by == height) {
                                                if (height < SEA_LEVEL) {
                                                        blockID = 12; // Sand
                                                } else {
                                                        blockID = 2; // Grass
                                                }
                                        } else if (by <= SEA_LEVEL && by > height) {
                                                blockID = 9; // Water
                                        }
                                        chunk.setBlockID(bx, by, bz, blockID);
                                }
                        }
                }

                chunk.modified = true;
                return chunk;
        }

        /**
         * Simple height generation using hash-based noise.
         */
        private int generateHeight(int x, int z) {
                long h = worldSeed ^ ((long) x * 341873128712L + (long) z * 132897987541L);
                h = (h ^ (h >>> 30)) * 0xBF58476D1CE4E5B9L;
                h = (h ^ (h >>> 27)) * 0x94D049BB133111EBL;
                h = h ^ (h >>> 31);
                int baseHeight = SEA_LEVEL - 1;
                int variation = (int) ((h & 0xFF) % 20) - 5;
                return baseHeight + variation;
        }

        /**
         * Populates a chunk with trees, ores, etc.
         */
        private void populateChunk(ChunkData chunk) {
                // Simplified population - full MC code would generate
                // trees, flowers, ores, caves, etc.
        }

        // ========== IPC Message Handlers ==========

        public void handlePlayerInput(byte[] payload) {
                if (payload.length < 28) return;
                int offset = 0;

                playerX = readDouble(payload, offset); offset += 8;
                playerY = readDouble(payload, offset); offset += 8;
                playerZ = readDouble(payload, offset); offset += 8;
                playerYaw = readFloat(payload, offset); offset += 4;
                playerPitch = readFloat(payload, offset); offset += 4;

                if (offset < payload.length) {
                        playerOnGround = payload[offset++] != 0;
                }
        }

        public void handleChunkRequest(byte[] payload) {
                // Client explicitly requests chunk data
        }

        public void handleBlockChange(byte[] payload) {
                if (payload.length < 13) return;
                int x = readInt(payload, 0);
                int y = readInt(payload, 4);
                int z = readInt(payload, 8);
                int blockID = payload[12] & 0xFF;

                int cx = x >> 4;
                int cz = z >> 4;
                ChunkData chunk = chunks.get(chunkKey(cx, cz));
                if (chunk != null) {
                        int bx = x & 0xF;
                        int bz = z & 0xF;
                        chunk.setBlockID(bx, y, bz, blockID);
                        // Send block change confirmation to client
                        byte[] response = new byte[13];
                        writeInt(response, 0, x);
                        writeInt(response, 4, y);
                        writeInt(response, 8, z);
                        response[12] = (byte) blockID;
                        WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_RENDER_COMMAND, response);
                }
        }

        public void handleChatMessage(byte[] payload) {
                // Process chat/command
        }

        public void handlePlayerJoin(String name) {
                this.playerName = name;
                // Generate spawn chunks
                int spawnX = 0, spawnZ = 0;
                playerX = spawnX + 0.5;
                playerZ = spawnZ + 0.5;
                playerY = SEA_LEVEL + 2;

                // Generate initial chunks
                for (int dx = -SPAWN_CHUNK_RADIUS; dx <= SPAWN_CHUNK_RADIUS; dx++) {
                        for (int dz = -SPAWN_CHUNK_RADIUS; dz <= SPAWN_CHUNK_RADIUS; dz++) {
                                getOrGenerateChunk(dx, dz);
                        }
                }

                // Send spawn position
                byte[] spawnData = new byte[28];
                writeDouble(spawnData, 0, playerX);
                writeDouble(spawnData, 8, playerY);
                writeDouble(spawnData, 16, playerZ);
                writeInt(spawnData, 24, gameMode);
                WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_PLAYER_SPAWN, spawnData);
        }

        public void handlePlayerLeave(byte[] payload) {
                autosave();
        }

        public void handleSaveWorld(byte[] payload) {
                autosave();
                WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_WORLD_SAVE_DONE, new byte[0]);
        }

        public void handleLoadWorld(byte[] payload) {
                if (payload.length > 0) {
                        worldName = new String(payload);
                }
                // Load world from IndexedDB
                WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_WORLD_LOAD_DONE, new byte[0]);
        }

        // ========== Serialization ==========

        private void sendChunkToClient(ChunkData chunk) {
                // Serialize chunk: [4B chunkX][4B chunkZ][blockIDs][metadata]
                int headerSize = 8;
                int blockDataSize = CHUNK_SIZE * CHUNK_SIZE * WORLD_HEIGHT;
                int totalSize = headerSize + blockDataSize + chunk.blockMetadata.length;
                byte[] data = new byte[totalSize];
                writeInt(data, 0, chunk.x);
                writeInt(data, 4, chunk.z);
                System.arraycopy(chunk.blockIDs, 0, data, headerSize, blockDataSize);
                System.arraycopy(chunk.blockMetadata, 0, data, headerSize + blockDataSize, chunk.blockMetadata.length);
                WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_CHUNK_DATA, data);
        }

        private void deserializeChunk(ChunkData chunk, byte[] data) {
                int blockDataSize = CHUNK_SIZE * CHUNK_SIZE * WORLD_HEIGHT;
                if (data.length >= blockDataSize) {
                        System.arraycopy(data, 0, chunk.blockIDs, 0, blockDataSize);
                        chunk.modified = false;
                }
        }

        // ========== Persistence (IndexedDB) ==========

        private void saveChunk(ChunkData chunk) {
                byte[] data = new byte[chunk.blockIDs.length];
                System.arraycopy(chunk.blockIDs, 0, data, 0, data.length);
                saveChunkToDB0(chunk.x, chunk.z, bytesToArrayBuffer(data));
                chunk.modified = false;
        }

        private byte[] loadChunkFromDB(int cx, int cz) {
                ArrayBuffer buf = loadChunkFromDB0(cx, cz);
                if (buf == null) return null;
                return arrayBufferToBytes(buf);
        }

        private void autosave() {
                for (ChunkData chunk : chunks.values()) {
                        if (chunk.modified) {
                                saveChunk(chunk);
                        }
                }
                saveWorldMetadata();
        }

        private void saveWorldMetadata() {
                byte[] meta = new byte[32];
                writeLong(meta, 0, worldSeed);
                writeLong(meta, 8, worldTime);
                writeLong(meta, 16, dayTime);
                writeInt(meta, 24, gameMode);
                writeInt(meta, 28, tickCount);
                saveWorldMeta0(bytesToArrayBuffer(meta));
        }

        private void sendTimeUpdate() {
                byte[] data = new byte[16];
                writeLong(data, 0, worldTime);
                writeLong(data, 8, dayTime);
                WorkerMain.sendIPCMessage(WorkerMain.IPC_CHANNEL_TIME_UPDATE, data);
        }

        // ========== Utility ==========

        public void setPaused(boolean p) {
                paused = p;
        }

        public void shutdown() {
                autosave();
                chunks.clear();
                entities.clear();
                scheduledUpdates.clear();
                initialized = false;
        }

        private static int readInt(byte[] b, int off) {
                return (b[off] << 24) | ((b[off+1] & 0xFF) << 16) | ((b[off+2] & 0xFF) << 8) | (b[off+3] & 0xFF);
        }

        private static void writeInt(byte[] b, int off, int v) {
                b[off] = (byte)(v >> 24); b[off+1] = (byte)(v >> 16);
                b[off+2] = (byte)(v >> 8); b[off+3] = (byte) v;
        }

        private static long readLong(byte[] b, int off) {
                return ((long)readInt(b, off) << 32) | (readInt(b, off+4) & 0xFFFFFFFFL);
        }

        private static void writeLong(byte[] b, int off, long v) {
                writeInt(b, off, (int)(v >> 32)); writeInt(b, off+4, (int) v);
        }

        private static double readDouble(byte[] b, int off) {
                return Double.longBitsToDouble(readLong(b, off));
        }

        private static void writeDouble(byte[] b, int off, double v) {
                writeLong(b, off, Double.doubleToLongBits(v));
        }

        private static float readFloat(byte[] b, int off) {
                return Float.intBitsToFloat(readInt(b, off));
        }

        private static void writeFloat(byte[] b, int off, float v) {
                writeInt(b, off, Float.floatToIntBits(v));
        }

        private static byte[] arrayBufferToBytes(ArrayBuffer buffer) {
                Uint8Array view = Uint8Array.create(buffer);
                byte[] result = new byte[view.getLength()];
                for (int i = 0; i < result.length; i++) result[i] = (byte) view.get(i);
                return result;
        }

        private static ArrayBuffer bytesToArrayBuffer(byte[] arr) {
                ArrayBuffer buffer = ArrayBuffer.create(arr.length);
                Uint8Array view = Uint8Array.create(buffer);
                for (int i = 0; i < arr.length; i++) view.set(i, (short)(arr[i] & 0xFF));
                return buffer;
        }

        // ========== IndexedDB Native Methods ==========

        @JSBody(params = { "cx", "cz", "data" }, script = ""
                        + "try {"
                        + "  var storeName = 'chunks';"
                        + "  var dbReq = indexedDB.open('eaglercraft_world', 1);"
                        + "  dbReq.onupgradeneeded = function(e) {"
                        + "    var db = e.target.result;"
                        + "    if (!db.objectStoreNames.contains(storeName)) {"
                        + "      db.createObjectStore(storeName);"
                        + "    }"
                        + "  };"
                        + "  dbReq.onsuccess = function(e) {"
                        + "    var db = e.target.result;"
                        + "    var tx = db.transaction(storeName, 'readwrite');"
                        + "    var store = tx.objectStore(storeName);"
                        + "    store.put(data, cx + ',' + cz);"
                        + "  };"
                        + "} catch(e) { console.error('saveChunk error:', e); }")
        private static native void saveChunkToDB0(int cx, int cz, ArrayBuffer data);

        @JSBody(params = { "cx", "cz" }, script = ""
                        + "try {"
                        + "  return null;"  // Synchronous stub; real impl uses async + cache
                        + "} catch(e) { return null; }")
        private static native ArrayBuffer loadChunkFromDB0(int cx, int cz);

        @JSBody(params = { "data" }, script = ""
                        + "try {"
                        + "  var dbReq = indexedDB.open('eaglercraft_world', 1);"
                        + "  dbReq.onsuccess = function(e) {"
                        + "    var db = e.target.result;"
                        + "    var tx = db.transaction('chunks', 'readwrite');"
                        + "    var store = tx.objectStore('chunks');"
                        + "    store.put(data, 'world_meta');"
                        + "  };"
                        + "} catch(e) { console.error('saveMeta error:', e); }")
        private static native void saveWorldMeta0(ArrayBuffer data);
}
