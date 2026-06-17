package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class IntByReference {
    public int value;

    public IntByReference() { this.value = 0; }
    public IntByReference(int value) { this.value = value; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public Pointer getPointer() { return null; }
}
