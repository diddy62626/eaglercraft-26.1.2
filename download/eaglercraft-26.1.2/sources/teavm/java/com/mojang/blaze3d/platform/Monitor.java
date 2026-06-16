package com.mojang.blaze3d.platform;

import org.teavm.jso.JSBody;

/**
 * EaglerCraft 26.1.2 browser override for Monitor.
 * Represents the screen/monitor in the browser environment.
 * Uses window.screen.width/height via JSBody for dimensions.
 */
public class Monitor {

	private final long monitorPointer;

	public Monitor() {
		this(0L);
	}

	public Monitor(long monitorPointer) {
		this.monitorPointer = monitorPointer;
	}

	/**
	 * Gets the screen width from window.screen.width.
	 */
	@JSBody(script = "return window.screen.width;")
	private static native int getScreenWidth0();

	/**
	 * Gets the screen height from window.screen.height.
	 */
	@JSBody(script = "return window.screen.height;")
	private static native int getScreenHeight0();

	/**
	 * Returns the screen width in pixels.
	 */
	public int getWidth() {
		return getScreenWidth0();
	}

	/**
	 * Returns the screen height in pixels.
	 */
	public int getHeight() {
		return getScreenHeight0();
	}

	/**
	 * Returns the monitor refresh rate. Browsers don't expose this,
	 * so we default to 60 Hz.
	 */
	public int getRefreshRate() {
		return 60;
	}

	/**
	 * Returns the current video mode of this monitor.
	 * Uses window.screen dimensions and default 60 Hz / 32 bpp.
	 */
	public VideoMode getCurrentMode() {
		return new VideoMode(getScreenWidth0(), getScreenHeight0(), 60, 32);
	}

	/**
	 * Returns a video mode for the given index.
	 * In the browser, we only have one "mode" (the current screen size).
	 */
	public VideoMode getMode(int index) {
		return getCurrentMode();
	}

	/**
	 * Returns the number of available video modes.
	 * In the browser, we return 1 (only the current mode).
	 */
	public int getModeCount() {
		return 1;
	}

	/**
	 * Returns the GLFW monitor pointer (unused in browser).
	 */
	public long getPointer() {
		return monitorPointer;
	}

	@Override
	public String toString() {
		return "Monitor[width=" + getWidth() + ", height=" + getHeight() + ", refreshRate=60]";
	}
}
