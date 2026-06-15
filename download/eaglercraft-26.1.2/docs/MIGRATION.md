# Migration Guide: EaglerCraft 1.8 → EaglercraftX 26.1.2

> A comprehensive guide for migrating projects, patches, and knowledge from EaglerCraft 1.8 to EaglercraftX 26.1.2.

This document covers everything that changed between Minecraft 1.8 and 26.1.2, and how to adapt your EaglerCraft projects accordingly.

---

## Table of Contents

- [Overview of Changes](#overview-of-changes)
- [What Changed Between MC 1.8 and 26.1.2](#what-changed-between-mc-18-and-2612)
- [Porting Patches from 1.8 Format](#porting-patches-from-18-format)
- [Networking Protocol Changes](#networking-protocol-changes)
- [Rendering Pipeline Changes](#rendering-pipeline-changes)
- [Item System Changes (NBT → Data Components)](#item-system-changes-nbt--data-components)
- [World Format Changes](#world-format-changes)
- [API Changes](#api-changes)
- [Common Migration Issues](#common-migration-issues)

---

## Overview of Changes

Minecraft 26.1.2 represents over a decade of evolution from 1.8. The codebase has been fundamentally restructured in several key areas:

| Area | MC 1.8 | MC 26.1.2 | Migration Difficulty |
|------|--------|-----------|---------------------|
| Source obfuscation | MCP required | Unobfuscated | ⭐ Easy (improvement) |
| Item serialization | NBT tags | Data components | ⭐⭐⭐ Hard |
| Rendering | Fixed-function GL | Shader-based | ⭐⭐⭐ Hard |
| Networking | Protocol 47 | Protocol 775 | ⭐⭐⭐ Hard |
| World format | Anvil (regional) | Anvil (enhanced) | ⭐⭐ Medium |
| Chunk format | Legacy | Paletted containers | ⭐⭐ Medium |
| Block states | Metadata integers | Property-based states | ⭐⭐ Medium |
| Resource packs | v1 | v4+ | ⭐ Easy |
| Sound system | PositionedSound | SoundInstance | ⭐ Easy |
| Data generators | None | DataGen API | ⭐ Easy |

---

## What Changed Between MC 1.8 and 26.1.2

### Deobfuscation Elimination

The most impactful change: **Minecraft 26.1.2 ships unobfuscated**. This means:

- **No MCP mappings** — Class names like `RenderPlayer` stay `RenderPlayer`, not `bxf.a`
- **No decompilation** — Source code is provided directly, no Fernflower needed
- **No re-obfuscation** — Patches apply to the actual source names
- **Simpler build** — The entire MCP pipeline is eliminated

**Migration impact:** Your 1.8 patches reference MCP names (like `field_12345_a`). These must be translated to the actual 26.1.2 source names. Since 26.1.2 is unobfuscated, the source names are human-readable.

### Java Version Requirements

| MC Version | Java Required | Key Language Features Used |
|------------|--------------|---------------------------|
| 1.8 | Java 8 | Lambdas, streams |
| 26.1.2 | Java 25 | Records, sealed classes, pattern matching, virtual threads, string templates |

Make sure your development environment uses JDK 25 or later.

### Block State System

MC 1.8 used integer metadata (0-15) for block variants. MC 26.1.2 uses property-based block states:

**1.8 (metadata):**
```java
// Stone types were metadata values
world.getBlockState(pos).getBlock().getMetaFromState(state);
// Stone=0, Granite=1, Polished Granite=2, Diorite=3, etc.
```

**26.1.2 (block states):**
```java
// Each variant is a separate block with properties
BlockState state = world.getBlockState(pos);
if (state.is(Blocks.GRANITE)) { ... }
if (state.is(Blocks.POLISHED_GRANITE)) { ... }
// Properties for directional blocks, etc.
Direction facing = state.getValue(BlockStateProperties.FACING);
```

### Resource Pack Format

The resource pack format version has increased significantly:

| Feature | MC 1.8 (v1) | MC 26.1.2 (v4+) |
|---------|-------------|-----------------|
| Pack format | 1 | 34+ |
| Models | Block/item JSON | Block/item JSON with transforms |
| Block states | variants | multipart + variants |
| Font | Bitmap only | Bitmap + TTF |
| Shaders | Fixed-function | Core/compat shader JSON |
| Sounds | sounds.json | sounds.json with weighted random |
| Textures | Power-of-2 required | Any size (NPOT supported) |
| Animation | mcmeta flipbook | mcmeta with interpolation |

---

## Porting Patches from 1.8 Format

### Step 1: Map MCP Names to Source Names

Since 1.8 patches reference MCP deobfuscated names and 26.1.2 uses the original source names, you need a name mapping:

```
1.8 MCP Name        →  26.1.2 Source Name
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
EntityRenderer       →  GameRenderer
RenderManager        →  EntityRenderDispatcher
Minecraft.getMinecraft() →  Minecraft.getInstance()
Block.getBlockById() →  BuiltInRegistries.BLOCK.byId()
Item.getItemById()   →  BuiltInRegistries.ITEM.byId()
```

### Step 2: Convert Patch Format

1.8 patches used a custom EaglerCraft patch format. 26.1.2 supports both unified diff and ECR:

**1.8 patch format:**
```
>DELETE
net/minecraft/client/renderer/EntityRenderer.java
>REPLACE
net/minecraft/client/renderer/EntityRenderer.java:145
-private static final boolean USE_VBO = false;
+private static final boolean USE_VBO = true; // EaglerCraft
```

**26.1.2 unified diff:**
```diff
--- a/net/minecraft/client/renderer/GameRenderer.java
+++ b/net/minecraft/client/renderer/GameRenderer.java
@@ -145,7 +145,7 @@
-    private static final boolean USE_VBO = false;
+    private static final boolean USE_VBO = true; // EaglerCraft: VBO required for WebGL2
```

**26.1.2 ECR (for larger changes):**
```
ECR/1.0
File: net/minecraft/client/renderer/GameRenderer.java
Operation: REPLACE_METHOD
Target: initRenderer()
---
private void initRenderer() {
    // Full replacement with EaglerCraft WebGL2 renderer
    this.renderer = new EaglerDeferredRenderer(this);
    // ...
}
```

### Step 3: Handle Structural Changes

Many classes were renamed, moved, or restructured between 1.8 and 26.1.2:

| 1.8 Class | 26.1.2 Class | Notes |
|-----------|-------------|-------|
| `net.minecraft.client.renderer.EntityRenderer` | `net.minecraft.client.renderer.GameRenderer` | Renamed |
| `net.minecraft.client.renderer.RenderManager` | `net.minecraft.client.renderer.entity.EntityRenderDispatcher` | Moved + renamed |
| `net.minecraft.client.renderer.block.model.ModelBakery` | `net.minecraft/client/resources/model/ModelBakery` | Moved |
| `net.minecraft.world.chunk.storage.AnvilChunkLoader` | `net.minecraft.world.level.chunk.storage.IOWorker` | Complete rewrite |
| `net.minecraft.util.text.TextComponentString` | `net.minecraft.network.chat.Component` | New text system |
| `net.minecraft.nbt.NBTTagCompound` | `net.minecraft.nbt.CompoundTag` | Renamed (all NBT classes) |
| `net.minecraft.init.Blocks` | `net.minecraft.world.level.block.Blocks` | Moved |
| `net.minecraft.init.Items` | `net.minecraft.world.item.Items` | Moved |

### Step 4: Update TeaVM Stubs

Any TeaVM class library stubs you created for 1.8 need to be updated:

- Check if the Java API you stubbed is now provided by TeaVM natively
- Update stubs for APIs that changed between Java 8 and Java 25
- Add new stubs for APIs used by 26.1.2 that aren't in TeaVM's classlib

---

## Networking Protocol Changes

### Protocol Version Jump

| Aspect | Protocol 47 (1.8) | Protocol 775 (26.1.2) |
|--------|-------------------|----------------------|
| Protocol version | 47 | 775 |
| Handshake states | 3 (Handshake, Login, Play) | 4 (Handshake, Login, Config, Play) |
| Compression | zlib (per-packet) | permessage-deflate (WebSocket frame) |
| Item serialization | NBT (Slot format) | Data components |
| Chunk format | Full chunk + light mask | Bitmask + paletted sections |
| Entity metadata | Integer index + type | Integer index + type (expanded) |
| Chat format | JSON TextComponent | JSON Component |
| Plugin channels | 256 char limit | Identifier namespaced |
| Login sequence | Simple | Multi-step with configuration |

### New Configuration Phase

MC 26.1.2 adds a **Configuration Phase** between Login and Play:

```
Client                          Server
  │                               │
  │── Login Start ───────────────►│
  │◄─ Login Success ─────────────│
  │                               │
  │◄─ Config: Plugin Message ────│  (brand, registry)
  │◄─ Config: Registry Data ─────│  (dynamic registries)
  │◄─ Config: Feature Flags ─────│  (feature flags)
  │◄─ Config: Known Packs ───────│  (resource pack sync)
  │── Config: Plugin Message ────►│  (client brand)
  │── Config: Known Packs ───────►│  (acknowledge)
  │◄─ Config: Finish ────────────│
  │── Config: Acknowledge ───────►│
  │                               │
  │◄─ Play: Join Game ───────────│  (enter play state)
```

**Migration impact:** Any EaglerCraft proxy code that handles login must be updated to support the configuration phase. Packets sent during configuration must be handled separately from play packets.

### Compression Changes

1.8 used per-packet zlib compression (threshold at 256 bytes). 26.1.2 uses `permessage-deflate` at the WebSocket frame level:

**1.8 (per-packet zlib):**
```java
// Each packet > threshold was individually compressed
if (packet.length > compressionThreshold) {
    out.writeVarInt(packet.length); // uncompressed length
    out.writeBytes(zlibCompress(packet));
} else {
    out.writeVarInt(0); // no compression
    out.writeBytes(packet);
}
```

**26.1.2 (WebSocket permessage-deflate):**
```javascript
// Compression is handled at the WebSocket frame level
// The permessage-deflate extension is negotiated during WebSocket upgrade
// All binary frames benefit from shared deflate context
const ws = new WebSocket(url, {
    permessage-deflate: {
        serverNoContextTakeover: false,
        clientNoContextTakeover: false,
        level: 6
    }
});
```

**Migration impact:** Remove any per-packet compression/decompression code. Let the WebSocket layer handle compression.

---

## Rendering Pipeline Changes

### Fixed-Function to Shader-Based

The most fundamental rendering change: 1.8 used OpenGL fixed-function pipeline (`glBegin`/`glEnd`, `glVertex3f`, etc.), while 26.1.2 uses shader programs exclusively.

**1.8 rendering (fixed-function):**
```java
GlStateManager.pushMatrix();
GlStateManager.translate(x, y, z);
GlStateManager.bindTexture(textureId);
GlStateManager.glBegin(GL_QUADS);
GlStateManager.glVertex3f(0, 0, 0);
GlStateManager.glTexCoord2f(0, 0);
GlStateManager.glVertex3f(1, 0, 0);
// ...
GlStateManager.glEnd();
GlStateManager.popMatrix();
```

**26.1.2 rendering (shader-based):**
```java
// Set up shader program
ShaderInstance shader = RenderSystem.getShader();
shader.setDefaultUniforms(VertexFormat.Mode.QUADS, format, modelView, projection);
shader.apply();

// Upload vertex data to VAO
BufferBuilder builder = tesselator.begin(VertexFormat.Mode.QUADS, format);
builder.addVertex(matrix, 0, 0, 0).setUv(0, 0).setColor(0xFFFFFFFF);
builder.addVertex(matrix, 1, 0, 0).setUv(1, 0).setColor(0xFFFFFFFF);
// ...

// Draw
BufferUploader.drawWithShader(builder);
shader.clear();
```

### EaglerCraft 1.8 vs 26.1.2 Adapter

In EaglerCraft 1.8, `EaglerAdapterGL30` mapped fixed-function GL calls to WebGL1:

```java
// 1.8: Fixed-function → WebGL1 mapping
public static void glTranslatef(float x, float y, float z) {
    // Matrix operations were done in JS
    glMatrix.pushTranslate(x, y, z);
}
```

In EaglercraftX 26.1.2, `EaglerAdapterGL30` maps shader-based rendering to WebGL2:

```java
// 26.1.2: Shader-based → WebGL2 mapping
public static int glCreateProgram() {
    return gl.createProgram();
}
public static void glUseProgram(int program) {
    gl.useProgram(programs.get(program));
}
public static void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value) {
    gl.uniformMatrix4fv(location, transpose, value);
}
```

### Deferred Rendering

26.1.2 adds a deferred rendering pipeline that doesn't exist in 1.8:

- **Geometry pass** → G-Buffer (albedo, normal, PBR material, depth)
- **Lighting pass** → SSAO, SSR, shadow mapping
- **Post-processing** → Bloom, tonemapping, FXAA

If you had custom rendering code in 1.8, you need to:
1. Convert all immediate-mode rendering to VAO-based rendering
2. Ensure your geometry outputs the required G-Buffer data (normals, PBR materials)
3. Use the shader system instead of fixed-function state

---

## Item System Changes (NBT → Data Components)

### The Big Change

MC 1.8 used NBT (Named Binary Tag) for all item data. MC 26.1.2 replaces this with **Data Components**, a typed, validated, and more efficient system.

### 1.8: NBT-Based Items

```java
// Creating an enchanted diamond sword in 1.8
ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
NBTTagCompound nbt = new NBTTagCompound();
NBTTagCompound display = new NBTTagCompound();
display.setString("Name", "§r§6Excalibur");
nbt.setTag("display", display);
NBTTagList enchantments = new NBTTagList();
NBTTagCompound sharpness = new NBTTagCompound();
sharpness.setShort("id", (short)16);  // Enchantment ID
sharpness.setShort("lvl", (short)5);   // Level 5
enchantments.appendTag(sharpness);
nbt.setTag("ench", enchantments);
sword.setTagCompound(nbt);
```

### 26.1.2: Data Component Items

```java
// Creating an enchanted diamond sword in 26.1.2
ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
sword.set(DataComponents.CUSTOM_NAME, Component.literal("Excalibur").withStyle(ChatFormatting.GOLD));
sword.set(DataComponents.ENCHANTMENTS, ItemEnchantments.builder()
    .set(Enchantments.SHARPNESS, 5)
    .build());
```

### Data Component Benefits

| Aspect | NBT (1.8) | Data Components (26.1.2) |
|--------|-----------|--------------------------|
| Type safety | No (everything is a tag) | Yes (typed components) |
| Validation | Manual | Automatic (codec system) |
| Serialization | Recursive tag structure | Flat component map |
| Network size | Large (string keys, tag headers) | Small (varint IDs, typed payloads) |
| Lookup | O(n) string comparison | O(1) integer key |
| Default values | Missing tag = default | Component presence = value |
| Mutability | Mutable tags | Immutable components |

### Network Serialization

Items on the network are now serialized as:

```
VarInt: item ID (0 = empty)
VarInt: component count
  [For each component:]
    VarInt: component type ID
    VarInt: component data length
    Bytes: component data (codec-encoded)
VarInt: removed component count (for overrides)
  [For each removed component:]
    VarInt: component type ID
```

### Migration for EaglerCraft Patches

Any code that reads or writes item NBT must be updated:

```java
// 1.8: Read enchantment from NBT
NBTTagList enchants = stack.getEnchantmentTagList();
for (int i = 0; i < enchants.tagCount(); i++) {
    NBTTagCompound tag = enchants.getCompoundTagAt(i);
    int id = tag.getShort("id");
    int level = tag.getShort("lvl");
}

// 26.1.2: Read enchantment from data component
ItemEnchantments enchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
for (Map.Entry<Holder<Enchantment>, Integer> entry : enchants.entrySet()) {
    Enchantment enchantment = entry.getKey().value();
    int level = entry.getValue();
}
```

---

## World Format Changes

### Chunk Format

MC 1.8 chunks used a simple array-based format. MC 26.1.2 uses **paletted containers** for much better memory efficiency:

**1.8 chunk section:**
```java
// Each section was a fixed 4096-entry array of block IDs + 2048 bytes of metadata
byte[] blockIds = new byte[4096];      // 1 byte per block
NibbleArray blockMeta = new NibbleArray(4096); // 4 bits per block
```

**26.1.2 chunk section:**
```java
// Paletted container: uses a palette + bit-packed array
// For sections with < 16 unique block states: uses a linear palette (4 bits/entry)
// For sections with < 256 unique block states: uses an indirect palette (variable bits)
// For sections with >= 256 unique block states: uses the global palette (15+ bits/entry)
PalettedContainer<BlockState> section = new PalettedContainer<>(
    BlockState.EMPTY,
    blockStateRegistry,
    PalettedContainer.Strategy.SECTION_STATES
);
```

### World Directory Structure

| File/Dir | 1.8 | 26.1.2 | Notes |
|----------|-----|--------|-------|
| `level.dat` | NBT | NBT (same format) | More fields in 26.1.2 |
| `region/*.mca` | Anvil | Anvil (enhanced) | Same basic format, new chunk encoding |
| `data/*.dat` | NBT | NBT | More data files |
| `entities/*.mca` | N/A | Entity region files | 26.1.2 stores entities separately |
| `datapacks/` | N/A | Data packs | 26.1.2 supports data packs natively |

### EaglerCraft Storage

In the browser, worlds are stored in IndexedDB using a custom format:

```
IndexedDB: eaglercraftx
  Object Store: worlds
    Key: "world_name/level.dat"    → Value: NBT binary
    Key: "world_name/region/r.0.0.mca" → Value: region binary
    Key: "world_name/entities/e.0.0.mca" → Value: entity region binary
    Key: "world_name/icon.png"     → Value: PNG binary
```

---

## API Changes

### Text Component System

```java
// 1.8: TextComponentString
ITextComponent text = new TextComponentString("Hello ")
    .appendSibling(new TextComponentString("World").setChatStyle(
        new ChatStyle().setColor(EnumChatFormatting.GOLD)));

// 26.1.2: Component
Component text = Component.literal("Hello ")
    .append(Component.literal("World").withStyle(ChatFormatting.GOLD));
```

### Registry System

```java
// 1.8: Integer ID lookups
Block block = Block.getBlockById(1);  // Stone
Item item = Item.getItemById(1);      // Stone item?

// 26.1.2: Registry system
Block block = BuiltInRegistries.BLOCK.byId(1);  // Or use Blocks.STONE directly
Item item = BuiltInRegistries.ITEM.byId(1);
ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
```

### Resource Location

```java
// 1.8: String-based
ResourceLocation loc = new ResourceLocation("textures/blocks/stone.png");

// 26.1.2: Same class, but now mandatory for all registry objects
ResourceLocation loc = ResourceLocation.withDefaultNamespace("textures/block/stone.png");
// Note: "blocks" → "block" (singular)
```

---

## Common Migration Issues

### Issue: "Class not found" after porting

**Cause:** Many classes were renamed or moved between 1.8 and 26.1.2.

**Solution:** Use the class mapping table in this guide. Search the 26.1.2 source for the class's functionality rather than its 1.8 name.

### Issue: Patch fails to apply

**Cause:** The source code has changed significantly; line numbers and context no longer match.

**Solution:** Re-create the patch against the 26.1.2 source rather than trying to port the 1.8 patch directly. The unobfuscated source makes this much easier.

### Issue: Rendering appears broken

**Cause:** Fixed-function GL calls are not supported in the WebGL2 shader pipeline.

**Solution:** Convert all rendering to use shader programs and VAO-based geometry. See the Rendering Pipeline Changes section.

### Issue: Item data lost after migration

**Cause:** NBT-based item data is not compatible with data components.

**Solution:** Write a conversion function that reads NBT and translates it to the appropriate data components. The data component system covers all previously NBT-based features.

### Issue: World fails to load

**Cause:** Chunk format changes (array-based → paletted containers).

**Solution:** Use the MC 26.1.2 world upgrade mechanism. Worlds from 1.8 are automatically upgraded when first loaded in 26.1.2. However, in EaglerCraft, you must implement the upgrade path yourself or import worlds through the vanilla server first.

### Issue: Network handshake fails

**Cause:** Protocol 775 requires the new configuration phase that didn't exist in protocol 47.

**Solution:** Update your proxy to handle the configuration phase. The EaglercraftXBungee and EaglercraftXVelocity plugins handle this automatically.

### Issue: SharedArrayBuffer not available

**Cause:** Missing COOP/COEP headers.

**Solution:** Ensure your web server sends these headers:
```
Cross-Origin-Opener-Policy: same-origin
Cross-Origin-Embedder-Policy: require-corp
```

### Issue: TeaVM compilation errors

**Cause:** MC 26.1.2 uses Java 25 features not supported by older TeaVM versions.

**Solution:** Use the TeaVM version included in the EaglerCraft repository. It has been patched to support all Java 25 language features used by MC 26.1.2.
