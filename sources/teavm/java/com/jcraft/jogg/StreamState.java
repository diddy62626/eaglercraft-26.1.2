package com.jcraft.jogg;

public class StreamState {
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
    public void wrote(int bytes) {}
    public int pageout(Page page) { return 0; }
    public int packetout(Packet packet) { return 0; }
    public void reset() {}
    public void writePage(Page page) {}

    public int pagein(com.jcraft.jogg.Page page) { return 0; }

    public void init(int state) {}
}
