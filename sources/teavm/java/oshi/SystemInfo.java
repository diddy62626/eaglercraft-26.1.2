package oshi;

import oshi.hardware.HardwareAbstractionLayer;

/**
 * TeaVM/browser stub for oshi.SystemInfo.
 *
 * MC's Startup Notification Manager uses oshi to query system info
 * (CPU, memory, OS) for telemetry. Browser environment can't access
 * this info directly, so we provide a stub that returns a singleton
 * HardwareAbstractionLayer with default/empty values.
 */
public class SystemInfo {
    private static final SystemInfo INSTANCE = new SystemInfo();

    public static SystemInfo create() { return INSTANCE; }

    public HardwareAbstractionLayer getHardware() {
        return new HardwareAbstractionLayer();
    }

    public oshi.software.os.OperatingSystem getOperatingSystem() {
        return new oshi.software.os.OperatingSystem();
    }
}
