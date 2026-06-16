package org.lwjgl.opengl;

/**
 * TeaVM-compatible shim for LWJGL's GL42 class.
 * Constants only; features not available in WebGL2.
 */
public class GL42 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Texture compression ---
	public static final int GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = 0x8E8E;
	public static final int GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 0x8E8F;
	public static final int GL_COMPRESSED_RGBA_BPTC_UNORM = 0x8E8C;
	public static final int GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = 0x8E8D;

	// --- Atomic counter ---
	public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92D5;
	public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;
	public static final int GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2;
	public static final int GL_ATOMIC_COUNTER_BUFFER_SIZE = 0x92C3;

	// --- Image load/store (no-ops) ---
	public static final int GL_READ_ONLY = 0x88B8;
	public static final int GL_WRITE_ONLY = 0x88B9;
	public static final int GL_READ_WRITE = 0x88BA;

	// --- Texture storage ---
	public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F;
	public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 0x82DF;

	// --- Shader image ---
	public static final int GL_MAX_VERTEX_IMAGE_UNIFORMS = 0x90CA;
	public static final int GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90CE;
	public static final int GL_MAX_COMBINED_IMAGE_UNIFORMS = 0x90CF;

	// ================================================================
	// No-op methods
	// ================================================================

	/**
	 * No-op: glMemoryBarrier not supported in WebGL2.
	 */
	public static void glMemoryBarrier(int barriers) {
		// Not supported in WebGL2
	}

	/**
	 * No-op: glBindImageTexture not supported in WebGL2.
	 */
	public static void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
		// Not supported in WebGL2
	}
}
