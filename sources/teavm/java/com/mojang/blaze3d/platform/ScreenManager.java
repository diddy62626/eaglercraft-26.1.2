package com.mojang.blaze3d.platform;

/**
 * EaglerCraft 26.1.2 browser override for ScreenManager.
 * In the browser, there is no multi-monitor window management.
 * All methods are stubs.
 */
public class ScreenManager {

	private final MonitorCreator monitorCreator;

	public ScreenManager(MonitorCreator monitorCreator) {
		this.monitorCreator = monitorCreator;
	}

	/**
	 * Returns the monitor that the given window is on.
	 * In the browser, we always return the primary monitor.
	 */
	public Monitor getMonitor(long window) {
		return monitorCreator.getPrimaryMonitor();
	}

	/**
	 * Returns the primary monitor.
	 */
	public Monitor getPrimaryMonitor() {
		return monitorCreator.getPrimaryMonitor();
	}

	/**
	 * Updates monitor information. No-op in browser.
	 */
	public void updateMonitorInfo() {
		// no-op in browser
	}

	/**
	 * Called when a monitor is connected. No-op in browser.
	 */
	public void onMonitorConnect(long monitor) {
		// no-op in browser
	}

	/**
	 * Called when a monitor is disconnected. No-op in browser.
	 */
	public void onMonitorDisconnect(long monitor) {
		// no-op in browser
	}

	/**
	 * Shutdown cleanup. No-op in browser.
	 */
	public void shutdown() {
		// no-op in browser
	}
}
