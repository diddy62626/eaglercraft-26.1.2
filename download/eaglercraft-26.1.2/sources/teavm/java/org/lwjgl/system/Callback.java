package org.lwjgl.system;

public abstract class Callback implements Pointer {
    private long address;

    protected Callback(long address) {
        this.address = address;
    }

    protected Callback() {
        this.address = 0L;
    }

    @Override
    public long address() {
        return address;
    }

    public void free() {
        // no-op in browser
    }
}
