package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class PointerByReference {
    public Pointer value;

    public PointerByReference() { this.value = null; }
    public PointerByReference(Pointer value) { this.value = value; }
    public Pointer getValue() { return value; }
    public void setValue(Pointer value) { this.value = value; }
    public Pointer getPointer() { return null; }
}
