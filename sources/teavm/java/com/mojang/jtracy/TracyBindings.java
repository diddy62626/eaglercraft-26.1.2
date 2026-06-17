package com.mojang.jtracy;

/**
 * EaglerCraft shadow stub for TracyBindings.
 * Replaces all native methods with no-op stubs.
 */
public class TracyBindings {
    public static int beginZone(String name, String function, String file, int line) { return 0; }
    public static void endZone(int zoneId) {}
    public static long mallocNamed(long size, long poolId, int flags) { return 0; }
    public static long freeNamed(long ptr, long poolId) { return 0; }
    public static long leakName(String name) { return 0; }
    public static void plotValue(String name, double value) {}
    public static void plotValue(String name, float value) {}
    public static void plotValue(String name, int value) {}
    public static void frameMark() {}
    public static void frameMarkStart(String name) {}
    public static void frameMarkEnd(String name) {}
    public static void setThreadName(String name) {}
    public static void message(String text, int color) {}
    public static void message(String text) {}
    public static void appInfo(String name, String value) {}
    public static void configureZones(boolean enabled) {}
    public static void configurePlots(boolean enabled) {}
    public static void configureMessages(boolean enabled) {}
    public static void configureMemory(boolean enabled) {}
    public static void configureCallstacks(boolean enabled) {}
    public static void configureContextSwitches(boolean enabled) {}
    public static void configureFrameImages(boolean enabled) {}
    public static void configureGpu(boolean enabled) {}
    public static void configureSampling(int frequency) {}
    public static void configureSampleCount(int count) {}
}
