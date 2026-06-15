package org.lwjgl.openal;

public class ALCCapabilities {
    public long device;

    public ALCCapabilities(long device) {
        this.device = device;
    }

    public ALCCapabilities() {
        this.device = 0L;
    }
}
