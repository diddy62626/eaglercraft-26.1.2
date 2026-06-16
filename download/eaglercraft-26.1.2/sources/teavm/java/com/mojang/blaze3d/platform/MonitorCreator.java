package com.mojang.blaze3d.platform;

import java.util.Collections;
import java.util.List;

/**
 * EaglerCraft 26.1.2 browser override for MonitorCreator.
 * In the browser, there is only one "monitor" (the screen).
 */
public class MonitorCreator {

	public MonitorCreator() {
	}

	/**
	 * Returns a Monitor for the given GLFW monitor pointer.
	 * In the browser, we always return the same single monitor.
	 */
	public Monitor getMonitor(long monitorPointer) {
		return new Monitor(monitorPointer);
	}

	/**
	 * Returns the list of available monitors.
	 * In the browser, we return a single monitor.
	 */
	public List<Monitor> getMonitors() {
		return Collections.singletonList(new Monitor(0L));
	}

	/**
	 * Returns the primary monitor.
	 */
	public Monitor getPrimaryMonitor() {
		return new Monitor(0L);
	}
}
