package org.lwjgl.opengl;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL13 class.
 * Forwards calls to PlatformOpenGL (WebGL2 backend).
 * Compressed texture methods are no-ops (not widely supported in WebGL2).
 */
public class GL13 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Texture units (duplicated from GL11 for convenience) ---
	public static final int GL_TEXTURE0 = 0x84C0;
	public static final int GL_TEXTURE1 = 0x84C1;
	public static final int GL_TEXTURE2 = 0x84C2;
	public static final int GL_TEXTURE3 = 0x84C3;
	public static final int GL_TEXTURE4 = 0x84C4;
	public static final int GL_TEXTURE5 = 0x84C5;
	public static final int GL_TEXTURE6 = 0x84C6;
	public static final int GL_TEXTURE7 = 0x84C7;
	public static final int GL_TEXTURE8 = 0x84C8;
	public static final int GL_TEXTURE9 = 0x84C9;
	public static final int GL_TEXTURE10 = 0x84CA;
	public static final int GL_TEXTURE11 = 0x84CB;
	public static final int GL_TEXTURE12 = 0x84CC;
	public static final int GL_TEXTURE13 = 0x84CD;
	public static final int GL_TEXTURE14 = 0x84CE;
	public static final int GL_TEXTURE15 = 0x84CF;
	public static final int GL_TEXTURE16 = 0x84D0;
	public static final int GL_TEXTURE17 = 0x84D1;
	public static final int GL_TEXTURE18 = 0x84D2;
	public static final int GL_TEXTURE19 = 0x84D3;
	public static final int GL_TEXTURE20 = 0x84D4;
	public static final int GL_TEXTURE21 = 0x84D5;
	public static final int GL_TEXTURE22 = 0x84D6;
	public static final int GL_TEXTURE23 = 0x84D7;
	public static final int GL_TEXTURE24 = 0x84D8;
	public static final int GL_TEXTURE25 = 0x84D9;
	public static final int GL_TEXTURE26 = 0x84DA;
	public static final int GL_TEXTURE27 = 0x84DB;
	public static final int GL_TEXTURE28 = 0x84DC;
	public static final int GL_TEXTURE29 = 0x84DD;
	public static final int GL_TEXTURE30 = 0x84DE;
	public static final int GL_TEXTURE31 = 0x84DF;
	public static final int GL_ACTIVE_TEXTURE = 0x84E0;

	// --- Multisample ---
	public static final int GL_MULTISAMPLE = 0x809D;
	public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809F;
	public static final int GL_SAMPLE_ALPHA_TO_ONE = 0x809F;
	public static final int GL_SAMPLE_COVERAGE = 0x80A0;
	public static final int GL_SAMPLE_BUFFERS = 0x80A8;
	public static final int GL_SAMPLES = 0x80A9;
	public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80AA;
	public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80AB;

	// --- Cube map ---
	public static final int GL_TEXTURE_CUBE_MAP = 0x8513;
	public static final int GL_TEXTURE_BINDING_CUBE_MAP = 0x8514;
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
	public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
	public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
	public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;

	// --- Compressed textures ---
	public static final int GL_COMPRESSED_TEXTURE_FORMATS = 0x86A3;
	public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2;
	public static final int GL_COMPRESSED_RGB = 0x84ED;
	public static final int GL_COMPRESSED_RGBA = 0x84EE;
	public static final int GL_TEXTURE_COMPRESSION_HINT = 0x84EF;

	// --- Wrap modes ---
	public static final int GL_CLAMP_TO_BORDER = 0x812D;

	// --- Combine / texture env ---
	public static final int GL_COMBINE = 0x8570;
	public static final int GL_COMBINE_RGB = 0x8571;
	public static final int GL_COMBINE_ALPHA = 0x8572;
	public static final int GL_RGB_SCALE = 0x8573;
	public static final int GL_ALPHA_SCALE = 0x8574;
	public static final int GL_INTERPOLATE = 0x8575;
	public static final int GL_SUBTRACT = 0x84E7;
	public static final int GL_SRC0_RGB = 0x8580;
	public static final int GL_SRC1_RGB = 0x8581;
	public static final int GL_SRC2_RGB = 0x8582;
	public static final int GL_SRC0_ALPHA = 0x8588;
	public static final int GL_SRC1_ALPHA = 0x8589;
	public static final int GL_SRC2_ALPHA = 0x858A;
	public static final int GL_OPERAND0_RGB = 0x8590;
	public static final int GL_OPERAND1_RGB = 0x8591;
	public static final int GL_OPERAND2_RGB = 0x8592;
	public static final int GL_OPERAND0_ALPHA = 0x8598;
	public static final int GL_OPERAND1_ALPHA = 0x8599;
	public static final int GL_OPERAND2_ALPHA = 0x859A;

	// --- DOT3 ---
	public static final int GL_DOT3_RGB = 0x86AE;
	public static final int GL_DOT3_RGBA = 0x86AF;

	// ================================================================
	// GL Methods
	// ================================================================

	public static void glActiveTexture(int texture) {
		PlatformOpenGL._wglActiveTexture(texture);
	}

	/**
	 * No-op: glCompressedTexImage2D is not broadly supported in WebGL2.
	 */
	public static void glCompressedTexImage2D(int target, int level, int internalformat,
			int width, int height, int border, int imageSize, Object data) {
		// Not broadly supported in WebGL2
	}

	/**
	 * No-op: glCompressedTexSubImage2D is not broadly supported in WebGL2.
	 */
	public static void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset,
			int width, int height, int format, int imageSize, Object data) {
		// Not broadly supported in WebGL2
	}

	/**
	 * No-op: glSampleCoverage not needed in WebGL2 shim.
	 */
	public static void glSampleCoverage(float value, boolean invert) {
		// Not needed
	}
}
