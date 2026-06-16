package org.lwjgl.openal;

public class ALCapabilities {
    public long context;

    public ALCapabilities(long context) {
        this.context = context;
    }

    public ALCapabilities() {
        this.context = 0L;
    }
}
