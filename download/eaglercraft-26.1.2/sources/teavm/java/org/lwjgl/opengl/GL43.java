package org.lwjgl.opengl;

/**
 * TeaVM-compatible shim for LWJGL's GL43 class.
 * glCopyImageSubData is a no-op since it's not supported in WebGL2.
 */
public class GL43 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Compute shader ---
	public static final int GL_COMPUTE_SHADER = 0x91B9;
	public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 0x91BB;
	public static final int GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91BC;
	public static final int GL_MAX_COMPUTE_IMAGE_UNIFORMS = 0x91BD;
	public static final int GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262;
	public static final int GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263;
	public static final int GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264;
	public static final int GL_MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265;
	public static final int GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266;
	public static final int GL_COMPUTE_LOCAL_WORK_SIZE = 0x8267;
	public static final int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;

	// --- Shader storage buffer ---
	public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;
	public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;
	public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD;
	public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D7;
	public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA;
	public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB;
	public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC;
	public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE;
	public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF;

	// --- Debug output ---
	public static final int GL_DEBUG_OUTPUT = 0x92E0;
	public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;
	public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243;
	public static final int GL_DEBUG_CALLBACK_FUNCTION = 0x8244;
	public static final int GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;
	public static final int GL_DEBUG_SOURCE_API = 0x8246;
	public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247;
	public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248;
	public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 0x8249;
	public static final int GL_DEBUG_SOURCE_APPLICATION = 0x824A;
	public static final int GL_DEBUG_SOURCE_OTHER = 0x824B;
	public static final int GL_DEBUG_TYPE_ERROR = 0x824C;
	public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D;
	public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824E;
	public static final int GL_DEBUG_TYPE_PORTABILITY = 0x824F;
	public static final int GL_DEBUG_TYPE_PERFORMANCE = 0x8250;
	public static final int GL_DEBUG_TYPE_OTHER = 0x8251;
	public static final int GL_DEBUG_TYPE_MARKER = 0x8268;
	public static final int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269;
	public static final int GL_DEBUG_TYPE_POP_GROUP = 0x826A;
	public static final int GL_DEBUG_SEVERITY_HIGH = 0x9146;
	public static final int GL_DEBUG_SEVERITY_MEDIUM = 0x9147;
	public static final int GL_DEBUG_SEVERITY_LOW = 0x9148;
	public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;
	public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 0x9143;
	public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 0x9144;
	public static final int GL_DEBUG_LOGGED_MESSAGES = 0x9145;

	// --- Copy image ---
	public static final int GL_TEXTURE_2D = 0x0DE1;
	public static final int GL_TEXTURE_3D = 0x806F;
	public static final int GL_TEXTURE_2D_ARRAY = 0x8C1A;
	public static final int GL_TEXTURE_CUBE_MAP = 0x8513;

	// ================================================================
	// GL Methods
	// ================================================================

	/**
	 * No-op: glCopyImageSubData is not supported in WebGL2.
	 * Log a warning if debug mode is desired.
	 */
	public static void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ,
			int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ,
			int srcWidth, int srcHeight, int srcDepth) {
		// Not supported in WebGL2 - no equivalent functionality available
		// This is used by some MC rendering paths; may need alternative implementation
	}

	/**
	 * No-op: glDispatchCompute not supported in WebGL2.
	 */
	public static void glDispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
		// Not supported in WebGL2
	}

	/**
	 * No-op: glMemoryBarrier not supported in WebGL2.
	 */
	public static void glMemoryBarrier(int barriers) {
		// Not supported in WebGL2
	}
}
