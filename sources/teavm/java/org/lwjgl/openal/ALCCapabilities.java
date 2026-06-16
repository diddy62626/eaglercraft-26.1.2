package org.lwjgl.openal;

public class ALCCapabilities {
    public long device;
    public boolean ALC_SOFT_HRTF = false;
    public boolean OpenALC11 = true;

    public ALCCapabilities(long device) {
        this.device = device;
    }

    public ALCCapabilities() {
        this.device = 0L;
    }
}
