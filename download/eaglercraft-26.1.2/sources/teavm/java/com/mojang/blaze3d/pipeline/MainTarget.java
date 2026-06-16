package com.mojang.blaze3d.pipeline;

/**
 * EaglerCraft 26.1.2 browser override for MainTarget.
 * The main render target represents the default framebuffer (the canvas).
 * In the browser, this is the WebGL2 default framebuffer (null).
 *
 * <p>In vanilla MC, MainTarget extends RenderTarget and adds MSAA support.
 * In the browser, we delegate to WebGL2's built-in framebuffer.</p>
 */
public class MainTarget extends RenderTarget {

	/** The singleton main target instance. */
	private static MainTarget instance;

	public MainTarget(int width, int height) {
		super(width, height, true, false);
	}

	/**
	 * Creates and returns the main render target.
	 *
	 * @param width  The framebuffer width
	 * @param height The framebuffer height
	 * @return The main render target instance
	 */
	public static MainTarget create(int width, int height) {
		if (instance == null) {
			instance = new MainTarget(width, height);
		} else {
			instance.resize(width, height);
		}
		return instance;
	}

	/**
	 * Gets the current main target instance.
	 */
	public static MainTarget get() {
		return instance;
	}

	/**
	 * Binds the main (default) framebuffer for writing.
	 * In WebGL2, binding null selects the default framebuffer.
	 */
	@Override
	public void bindWrite(boolean setViewport) {
		net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL._wglBindFramebuffer(
			net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext.FRAMEBUFFER, null);
		if (setViewport) {
			net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL._wglViewport(0, 0, width, height);
		}
	}

	/**
	 * Binds the main framebuffer for reading.
	 */
	@Override
	public void bindRead() {
		net.lax1dude.eaglercraft.v2_6.internal.PlatformOpenGL._wglBindFramebuffer(
			net.lax1dude.eaglercraft.v2_6.internal.teavm.WebGL2RenderingContext.READ_FRAMEBUFFER, null);
	}

	/**
	 * The main target is always bound to the default framebuffer.
	 */
	@Override
	public boolean isBound() {
		return true;
	}

	/**
	 * Returns the actual framebuffer ID for the main target.
	 * Returns 0 (default framebuffer) in WebGL2.
	 */
	@Override
	public int getFrameBufferId() {
		return 0;
	}
}
