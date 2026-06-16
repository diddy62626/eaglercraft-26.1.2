package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;

public class Unknown {
    public Unknown() {}
    public Pointer getPointer() { return null; }
    public int AddRef() { return 0; }
    public int Release() { return 0; }
    public int QueryInterface(com.sun.jna.ptr.PointerByReference ref) { return 0; }
}
