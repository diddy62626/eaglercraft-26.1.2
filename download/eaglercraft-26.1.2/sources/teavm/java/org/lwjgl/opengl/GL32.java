package org.lwjgl.opengl;

/**
 * TeaVM-compatible shim for LWJGL's GL32 class.
 * Minimal constants only; geometry shaders and related features
 * are not supported in WebGL2.
 */
public class GL32 {

	// ================================================================
	// GL Constants
	// ================================================================

	public static final int GL_GEOMETRY_SHADER = 0x8DD9;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 0x8DA7;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 0x8DA8;
	public static final int GL_GEOMETRY_VERTICES_OUT = 0x8916;
	public static final int GL_GEOMETRY_INPUT_TYPE = 0x8917;
	public static final int GL_GEOMETRY_OUTPUT_TYPE = 0x8918;
	public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 0x8C29;
	public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 0x8DDF;
	public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 0x8DE0;
	public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8DE1;
	public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122;
	public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125;
	public static final int GL_LINES_ADJACENCY = 0x000A;
	public static final int GL_LINE_STRIP_ADJACENCY = 0x000B;
	public static final int GL_TRIANGLES_ADJACENCY = 0x000C;
	public static final int GL_TRIANGLE_STRIP_ADJACENCY = 0x000D;

	// --- Sync objects ---
	public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;
	public static final int GL_SYNC_FENCE = 0x9116;
	public static final int GL_UNSIGNALED = 0x9118;
	public static final int GL_SIGNALED = 0x9119;
	public static final int GL_ALREADY_SIGNALED = 0x911A;
	public static final int GL_TIMEOUT_EXPIRED = 0x911B;
	public static final int GL_CONDITION_SATISFIED = 0x911C;
	public static final int GL_WAIT_FAILED = 0x911D;
	public static final int GL_OBJECT_TYPE = 0x9112;
	public static final int GL_SYNC_STATUS = 0x9114;
	public static final int GL_SYNC_CONDITION = 0x9113;
	public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 0x00000001;

	// --- Image access (no-ops) ---
	public static final int GL_READ_ONLY = 0x88B8;
	public static final int GL_WRITE_ONLY = 0x88B9;
	public static final int GL_READ_WRITE = 0x88BA;

	// ================================================================
	// No-op methods (geometry shader not supported in WebGL2)
	// ================================================================

	/**
	 * No-op: glFramebufferTexture not supported in WebGL2.
	 */
	public static void glFramebufferTexture(int target, int attachment, int texture, int level) {
		// Not supported in WebGL2 - use glFramebufferTextureLayer instead
	}

	/**
	 * No-op: glBeginQuery not directly in this shim layer.
	 * Use PlatformOpenGL directly if needed.
	 */
}
