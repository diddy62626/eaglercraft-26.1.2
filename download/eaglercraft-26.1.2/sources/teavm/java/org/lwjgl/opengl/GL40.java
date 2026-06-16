package org.lwjgl.opengl;

/**
 * TeaVM-compatible shim for LWJGL's GL40 class.
 * Minimal constants only; tessellation shaders are not supported in WebGL2.
 */
public class GL40 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Tessellation ---
	public static final int GL_PATCHES = 0x000E;
	public static final int GL_TESS_CONTROL_SHADER = 0x8E88;
	public static final int GL_TESS_EVALUATION_SHADER = 0x8E87;
	public static final int GL_PATCH_VERTICES = 0x8E72;
	public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73;
	public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;
	public static final int GL_MAX_PATCH_VERTICES = 0x8E7B;
	public static final int GL_MAX_TESS_GEN_LEVEL = 0x8E7C;
	public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F;
	public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80;
	public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81;
	public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82;
	public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83;
	public static final int GL_MAX_TESS_PATCH_COMPONENTS = 0x8E84;
	public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85;
	public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86;

	// --- Draw indirect ---
	public static final int GL_DRAW_INDIRECT_BUFFER = 0x8F3F;

	// --- Sample shading ---
	public static final int GL_MIN_SAMPLE_SHADING_VALUE = 0x8C37;
	public static final int GL_SAMPLE_SHADING = 0x8C36;

	// ================================================================
	// No-op methods (tessellation not supported in WebGL2)
	// ================================================================

	/**
	 * No-op: glPatchParameteri not supported in WebGL2.
	 */
	public static void glPatchParameteri(int pname, int value) {
		// Not supported in WebGL2
	}

	/**
	 * No-op: glPatchParameterfv not supported in WebGL2.
	 */
	public static void glPatchParameterfv(int pname, float[] values) {
		// Not supported in WebGL2
	}
}
