package org.lwjgl.openal;

public class ALCapabilities {
    public long context;
    public boolean AL_EXT_LINEAR_DISTANCE = false;

    public ALCapabilities(long context) {
        this.context = context;
    }

    public ALCapabilities() {
        this.context = 0L;
    }
}
