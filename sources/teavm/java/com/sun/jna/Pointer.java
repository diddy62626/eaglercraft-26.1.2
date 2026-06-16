package com.sun.jna;

public class Pointer {
    public static final Pointer NULL = null;
    public long peer = 0L;

    public Pointer() {}
    public Pointer(long peer) { this.peer = peer; }

    public long getLong(int offset) { return 0L; }
    public int getInt(int offset) { return 0; }
    public void setLong(int offset, long value) {}
    public void setInt(int offset, int value) {}
    public Pointer getPointer(int offset) { return null; }
    public void write(int offset, byte[] buf, int index, int length) {}
    public void read(int offset, byte[] buf, int index, int length) {}
    public byte getByte(int offset) { return 0; }
    public void setMemory(long offset, long size, byte value) {}
    public String getString(int offset) { return ""; }
    public String getWideString(int offset) { return ""; }
    public static Pointer createConstant(long peer) { return new Pointer(peer); }
    public static Pointer createConstant(int peer) { return new Pointer(peer); }
}
