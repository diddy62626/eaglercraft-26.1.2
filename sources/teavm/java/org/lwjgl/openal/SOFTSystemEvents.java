package org.lwjgl.openal;

public final class SOFTSystemEvents {
    public static void alcEventProcessSOFT(long device) {}
    public static void alcEventInterceptorSOFT(long device, int eventType, int deviceType, long event, int length, long message) {}

    public static boolean alcEventControlSOFT(int[] events, boolean enable) { return false; }
    public static int alcEventIsSupportedSOFT(int eventType, int eventCategory) { return 0; }

    public static void alcEventCallbackSOFT(SOFTSystemEventProcI callback, long device) {}
}
