package com.sun.jna;

import java.io.IOException;

public class NativeLibrary {
    public String getName() { return ""; }
    public String getFile() { return ""; }
    public long getHandle() { return 0L; }
    public static NativeLibrary getInstance(String name) { return new NativeLibrary(); }
    public Function getFunction(String name) { return new Function(); }
    public static NativeLibrary getProcess() { return new NativeLibrary(); }
    public void dispose() {}
    public static final int OPTION_OPEN_FLAGS = 1;
}
