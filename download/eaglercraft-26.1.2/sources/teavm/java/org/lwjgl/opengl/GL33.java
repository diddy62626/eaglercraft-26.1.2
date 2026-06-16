package org.lwjgl.opengl;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;

/**
 * TeaVM-compatible shim for LWJGL's GL33 class.
 * Provides vertex attrib divisor and texture swizzle support.
 */
public class GL33 {

	// ================================================================
	// GL Constants
	// ================================================================

	// --- Texture swizzle ---
	public static final int GL_TEXTURE_SWIZZLE_R = 0x8E42;
	public static final int GL_TEXTURE_SWIZZLE_G = 0x8E43;
	public static final int GL_TEXTURE_SWIZZLE_B = 0x8E44;
	public static final int GL_TEXTURE_SWIZZLE_A = 0x8E45;
	public static final int GL_TEXTURE_SWIZZLE_RGBA = 0x8E46;

	// --- Instanced rendering ---
	public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE;

	// --- Sampler objects ---
	public static final int GL_SAMPLER_BINDING = 0x8919;

	// --- Primitive restart ---
	public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69;

	// --- Timestamp ---
	public static final int GL_TIMESTAMP = 0x8E28;

	// --- Query ---
	public static final int GL_TIME_ELAPSED = 0x88BF;

	// --- Buffer ---
	public static final int GL_RGB10_A2UI = 0x906F;

	// ================================================================
	// GL Methods
	// ================================================================

	public static void glVertexAttribDivisor(int index, int divisor) {
		PlatformOpenGL._wglVertexAttribDivisor(index, divisor);
	}

	/**
	 * Convenience alias for glBindVertexArray (same as GL30).
	 */
	public static void glBindVertexArray(int array) {
		GL30.glBindVertexArray(array);
	}

	/**
	 * Convenience alias for glGenVertexArrays (same as GL30).
	 */
	public static int glGenVertexArrays() {
		return GL30.glGenVertexArrays();
	}

	/**
	 * Convenience alias for glDeleteVertexArrays (same as GL30).
	 */
	public static void glDeleteVertexArrays(int array) {
		GL30.glDeleteVertexArrays(array);
	}

	/**
	 * No-op: glBindFragDataLocation not supported in WebGL2.
	 * WebGL2 uses layout(location=N) in fragment shaders instead.
	 */
	public static void glBindFragDataLocation(int program, int colorNumber, String name) {
		// Not supported in WebGL2; use layout qualifiers in shaders
	}

	/**
	 * No-op: glGetFragDataLocation not supported in WebGL2.
	 */
	public static int glGetFragDataLocation(int program, String name) {
		// Not supported in WebGL2
		return -1;
	}

	/**
	 * Texture swizzle is handled via glTexParameteri with the swizzle pnames.
	 * WebGL2 does support texture swizzle for 2D textures.
	 */
	public static void glTexParameteri(int target, int pname, int param) {
		PlatformOpenGL._wglTexParameteri(target, pname, param);
	}
}
