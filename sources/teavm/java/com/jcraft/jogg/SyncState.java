package com.jcraft.jogg;

public class SyncState {
    public byte[] data;
    public int storage;
    public int fill;
    public int returned;
    public int unsynced;
    public int headerbytes;
    public int bodybytes;

    public void init() {}
    public void clear() {}
    public int buffer(int size) { return 0; }
    public int wrote(int bytes) { return 0; }
    public int sync(int bytes) { return 0; }
    public int pageout(Page page) { return 0; }
    public void reset() {}
    public void writePage(Page page) {}
}
