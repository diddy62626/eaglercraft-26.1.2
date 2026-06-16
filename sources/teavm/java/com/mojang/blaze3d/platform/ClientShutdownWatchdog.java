package com.mojang.blaze3d.platform;

/**
 * EaglerCraft 26.1.2 browser override for ClientShutdownWatchdog.
 * This is a server-side class in vanilla MC that monitors for client hangs
 * during shutdown. In the browser, there is no separate server process,
 * so this is entirely a no-op.
 */
public class ClientShutdownWatchdog {

	public ClientShutdownWatchdog() {
	}

	/**
	 * Starts the shutdown watchdog. No-op in browser.
	 */
	public static void start() {
		// no-op in browser
	}

	/**
	 * Stops the shutdown watchdog. No-op in browser.
	 */
	public static void stop() {
		// no-op in browser
	}

	/**
	 * Notifies the watchdog that a shutdown step completed. No-op in browser.
	 */
	public static void notifyShutdownStep() {
		// no-op in browser
	}

	/**
	 * Checks if the client is shutting down. Always returns false in browser.
	 */
	public static boolean isShuttingDown() {
		return false;
	}
}
