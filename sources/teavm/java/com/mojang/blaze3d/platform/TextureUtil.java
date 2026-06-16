package com.mojang.blaze3d.platform;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSObject;

import net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext;

/**
 * EaglerCraft 26.1.2 browser override for TextureUtil.
 * Provides WebGL2 texture creation, deletion, and initialization utilities.
 * Maps integer texture IDs to WebGL2 texture JSObjects.
 */
public class TextureUtil {

	/** Texture ID to WebGL texture JSObject mapping. */
	private static final Map<Integer, JSObject> textureMap = new HashMap<>();

	/** Next available texture ID. */
	private static int nextTextureId = 1;

	public TextureUtil() {
	}

	/**
	 * Generates a new texture ID and creates the corresponding WebGL texture.
	 *
	 * @return The integer texture ID
	 */
	public static int generateTextureId() {
		WebGL2RenderingContext gl = ClientMain.getWebGL2();
		if (gl == null) return 0;

		int id = nextTextureId++;
		JSObject texture = gl.createTexture();
		textureMap.put(id, texture);
		return id;
	}

	/**
	 * Releases a texture ID and deletes the corresponding WebGL texture.
	 *
	 * @param textureId The texture ID to release
	 */
	public static void releaseTextureId(int textureId) {
		WebGL2RenderingContext gl = ClientMain.getWebGL2();
		JSObject texture = textureMap.remove(textureId);
		if (gl != null && texture != null) {
			gl.deleteTexture(texture);
		}
	}

	/**
	 * Gets the WebGL texture JSObject for the given integer texture ID.
	 *
	 * @param textureId The texture ID
	 * @return The WebGL texture JSObject, or null
	 */
	public static JSObject getTextureObject(int textureId) {
		return textureMap.get(textureId);
	}

	/**
	 * Prepares an image (texture) with the given dimensions.
	 * Binds the texture and allocates storage via texImage2D.
	 *
	 * @param textureId The texture ID
	 * @param width     The texture width
	 * @param height    The texture height
	 */
	public static void prepareImage(int textureId, int width, int height) {
		prepareImage(NativeImage.InternalGlFormat.RGBA, textureId, 0, width, height);
	}

	/**
	 * Prepares an image (texture) with the given format and dimensions.
	 * Binds the texture and allocates storage via texImage2D.
	 *
	 * @param format    The internal GL format
	 * @param textureId The texture ID
	 * @param maxLevel  The maximum mipmap level
	 * @param width     The texture width
	 * @param height    The texture height
	 */
	public static void prepareImage(NativeImage.InternalGlFormat format, int textureId, int maxLevel, int width, int height) {
		WebGL2RenderingContext gl = ClientMain.getWebGL2();
		JSObject texture = textureMap.get(textureId);
		if (gl == null || texture == null) return;

		gl.bindTexture(WebGL2RenderingContext.TEXTURE_2D, texture);

		// Set texture parameters
		gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D,
			WebGL2RenderingContext.TEXTURE_MIN_FILTER,
			maxLevel > 0 ? WebGL2RenderingContext.LINEAR_MIPMAP_LINEAR : WebGL2RenderingContext.NEAREST);
		gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D,
			WebGL2RenderingContext.TEXTURE_MAG_FILTER, WebGL2RenderingContext.NEAREST);
		gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D,
			WebGL2RenderingContext.TEXTURE_WRAP_S, WebGL2RenderingContext.CLAMP_TO_EDGE);
		gl.texParameteri(WebGL2RenderingContext.TEXTURE_2D,
			WebGL2RenderingContext.TEXTURE_WRAP_T, WebGL2RenderingContext.CLAMP_TO_EDGE);

		// Allocate texture storage
		int internalFormat = getInternalFormat(format);
		gl.texImage2D(WebGL2RenderingContext.TEXTURE_2D, 0, internalFormat,
			width, height, 0, format.glFormat(), WebGL2RenderingContext.UNSIGNED_BYTE, null);

		// Generate mipmaps if needed
		if (maxLevel > 0) {
			for (int level = 1; level <= maxLevel; level++) {
				int mipW = Math.max(1, width >> level);
				int mipH = Math.max(1, height >> level);
				gl.texImage2D(WebGL2RenderingContext.TEXTURE_2D, level, internalFormat,
					mipW, mipH, 0, format.glFormat(), WebGL2RenderingContext.UNSIGNED_BYTE, null);
			}
		}
	}

	/**
	 * Maps a NativeImage.InternalGlFormat to a WebGL2 internal format.
	 */
	private static int getInternalFormat(NativeImage.InternalGlFormat format) {
		switch (format) {
			case RGBA:
				return WebGL2RenderingContext.RGBA8;
			case RGB:
				return WebGL2RenderingContext.RGB8;
			case LUMINANCE_ALPHA:
				return WebGL2RenderingContext.LUMINANCE_ALPHA;  // WebGL2 still supports this
			case LUMINANCE:
				return WebGL2RenderingContext.LUMINANCE;
			default:
				return WebGL2RenderingContext.RGBA8;
		}
	}

	/**
	 * Reads a resource as an InputStream. Returns null in the browser
	 * since resources are loaded via the EPK system.
	 */
	public static InputStream readResourceAsStream(String path) {
		return null;
	}

	/**
	 * Writes image data as PNG. No-op in browser - use download API instead.
	 */
	public static void writeAsPNG(String path, int textureId, int width, int height, int mipmapLevel) {
		// no-op in browser
	}

	/**
	 * Initializes texture stub. No-op in browser.
	 */
	public static void initTextureStubs() {
		// no-op in browser
	}

	/**
	 * Gets the texture ID count (for debugging).
	 */
	public static int getTextureCount() {
		return textureMap.size();
	}

	// ========== MC 26.1.2 additional methods ==========

	/**
	 * MC 26.1.2: Reads an InputStream into a direct ByteBuffer.
	 */
	public static java.nio.ByteBuffer readResource(java.io.InputStream stream) throws java.io.IOException {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int n;
		while ((n = stream.read(buf)) > 0) {
			baos.write(buf, 0, n);
		}
		byte[] data = baos.toByteArray();
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * MC 26.1.2: Fills empty (fully transparent) areas of a NativeImage with dark color.
	 */
	public static void fillEmptyAreasWithDarkColor(com.mojang.blaze3d.platform.NativeImage image) {
		// Browser: WebGL2 textures handle alpha natively, no-op
	}

	/**
	 * MC 26.1.2: Makes a NativeImage fully opaque by replacing transparent pixels
	 * with the average of nearby opaque pixels (or black).
	 */
	public static void solidify(com.mojang.blaze3d.platform.NativeImage image) {
		// Browser: no-op
	}
}
