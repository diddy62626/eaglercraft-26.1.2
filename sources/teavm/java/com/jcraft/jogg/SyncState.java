package com.jcraft.jogg;

public class SyncState {
    public int state;
    public int unsynced;
    public void init() {}
    public void clear() {}
    public int buffer(int size) { return 0; }
    public void wrote(int bytes) {}
    public int sync(int bytes) { return 0; }
    public int pageout(com.jcraft.jogg.Page page) { return 0; }
    public void reset() {}
}
