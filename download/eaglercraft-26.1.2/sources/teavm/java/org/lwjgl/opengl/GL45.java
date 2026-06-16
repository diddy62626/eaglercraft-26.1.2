package org.lwjgl.opengl;

/**
 * TeaVM-compatible shim for LWJGL's GL45 class.
 * Minimal constants only; direct state access not supported in WebGL2.
 */
public class GL45 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Shader storage buffer ---
	public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;
	public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;

	// --- Clip distance ---
	public static final int GL_MAX_CLIP_DISTANCES = 0x0D32;
	public static final int GL_MAX_CULL_DISTANCES = 0x82F9;
	public static final int GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 0x82FA;

	// --- Direct state access (no-op methods, WebGL2 doesn't support) ---
	public static final int GL_QUERY_BUFFER = 0x9192;
	public static final int GL_QUERY_BUFFER_BINDING = 0x9193;

	// --- Texture view ---
	public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 0x82DB;
	public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 0x82DC;
	public static final int GL_TEXTURE_VIEW_MIN_LAYER = 0x82DD;
	public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 0x82DE;

	// --- GL_ARB_robustness ---
	public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT = 0x00000004;

	// ================================================================
	// No-op methods (DSA not supported in WebGL2)
	// ================================================================

	/**
	 * No-op: glCreateBuffers not supported in WebGL2 (DSA).
	 */
	public static void glCreateBuffers(int n, int[] buffers, int offset) {
		// DSA not available; use GL15.glGenBuffers instead
		for (int i = 0; i < n; i++) {
			buffers[offset + i] = GL15.glGenBuffers();
		}
	}

	/**
	 * No-op: glCreateFramebuffers not supported in WebGL2 (DSA).
	 */
	public static void glCreateFramebuffers(int n, int[] framebuffers, int offset) {
		// DSA not available; use GL30.glGenFramebuffers instead
		for (int i = 0; i < n; i++) {
			framebuffers[offset + i] = GL30.glGenFramebuffers();
		}
	}

	/**
	 * No-op: glCreateTextures not supported in WebGL2 (DSA).
	 */
	public static void glCreateTextures(int target, int n, int[] textures, int offset) {
		// DSA not available; use GL11.glGenTextures instead
		for (int i = 0; i < n; i++) {
			textures[offset + i] = GL11.glGenTextures();
		}
	}

	/**
	 * No-op: glCreateVertexArrays not supported in WebGL2 (DSA).
	 */
	public static void glCreateVertexArrays(int n, int[] arrays, int offset) {
		// DSA not available; use GL30.glGenVertexArrays instead
		for (int i = 0; i < n; i++) {
			arrays[offset + i] = GL30.glGenVertexArrays();
		}
	}

	/**
	 * No-op: glNamedBufferData not supported in WebGL2 (DSA).
	 * Falls back to bind+bufferData pattern.
	 */
	public static void glNamedBufferData(int buffer, long size, int usage) {
		int prev = 0; // Cannot read current binding in this shim
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, usage);
		// Cannot restore previous binding without query support
	}

	/**
	 * No-op: glTextureParameteri not supported in WebGL2 (DSA).
	 * Falls back to bind+texParameteri pattern.
	 */
	public static void glTextureParameteri(int texture, int pname, int param) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, pname, param);
	}

	/**
	 * No-op: glTextureSubImage2D not supported in WebGL2 (DSA).
	 * Falls back to bind+texSubImage2D pattern.
	 */
	public static void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset,
			int width, int height, int format, int type, Object pixels) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, level, xoffset, yoffset, width, height, format, type, pixels);
	}
}
