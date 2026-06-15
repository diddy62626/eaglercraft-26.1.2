package org.lwjgl.opengl;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL14 class.
 * Forwards blend-related calls to PlatformOpenGL (WebGL2 backend).
 */
public class GL14 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- BlendFuncSeparate ---
	public static final int GL_BLEND_DST_RGB = 0x80C8;
	public static final int GL_BLEND_SRC_RGB = 0x80C9;
	public static final int GL_BLEND_DST_ALPHA = 0x80CA;
	public static final int GL_BLEND_SRC_ALPHA = 0x80CB;

	// --- BlendEquation ---
	public static final int GL_FUNC_ADD = 0x8006;
	public static final int GL_FUNC_SUBTRACT = 0x800A;
	public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
	public static final int GL_MIN = 0x8007;
	public static final int GL_MAX = 0x8008;

	// --- Blend color ---
	public static final int GL_BLEND_COLOR = 0x8005;

	// --- Depth bounds (no-op in WebGL2) ---
	public static final int GL_DEPTH_BOUNDS_TEST_SGIS = 0x88F9;

	// --- Fog coordinate (no-op in WebGL2) ---
	public static final int GL_FOG_COORD_SRC = 0x8450;
	public static final int GL_FOG_COORD = 0x8451;
	public static final int GL_FRAGMENT_DEPTH = 0x8452;

	// --- Multi-sample ---
	public static final int GL_MULTISAMPLE = 0x809D;

	// ================================================================
	// GL Methods
	// ================================================================

	public static void glBlendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
		PlatformOpenGL._wglBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
	}

	public static void glBlendEquation(int mode) {
		PlatformOpenGL._wglBlendEquation(mode);
	}

	public static void glBlendEquationSeparate(int modeRGB, int modeAlpha) {
		PlatformOpenGL._wglBlendEquationSeparate(modeRGB, modeAlpha);
	}

	/**
	 * No-op: glBlendColor handled by GL11 where available.
	 */
	public static void glBlendColor(float red, float green, float blue, float alpha) {
		PlatformOpenGL._wglBlendColor(red, green, blue, alpha);
	}

	/**
	 * No-op: glDepthBoundsEXT not supported in WebGL2.
	 */
	public static void glDepthBoundsEXT(double zmin, double zmax) {
		// Not supported in WebGL2
	}
}
