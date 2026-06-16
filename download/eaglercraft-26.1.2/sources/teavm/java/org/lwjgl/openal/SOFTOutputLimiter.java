package org.lwjgl.openal;

/**
 * TeaVM-compatible stub for SOFTOutputLimiter extension.
 */
public class SOFTOutputLimiter {
    public static final int ALC_OUTPUT_LIMITER_SOFT = 0xD335;

    public static boolean alcResetDeviceSOFT(long device, int[] attrs) {
        return true;
    }
}
