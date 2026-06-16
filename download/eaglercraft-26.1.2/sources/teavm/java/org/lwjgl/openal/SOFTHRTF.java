package org.lwjgl.openal;

/**
 * TeaVM-compatible stub for SOFTHRTF extension.
 */
public class SOFTHRTF {
    public static final int ALC_HRTF_SOFT = 0xD333;
    public static final int ALC_HRTF_ID_SOFT = 0xD336;

    public static boolean alcResetDeviceSOFT(long device, int[] attrs) {
        return true;
    }
}
