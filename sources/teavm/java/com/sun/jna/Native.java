package com.sun.jna;

public final class Native {
    public static long register(String name) { return 0L; }
    public static void unregister() {}
    public static Library loadLibrary(String name, Class<?> interfaceClass) { return null; }
    public static Object load(String name, Class<?> interfaceClass) { return null; }
    public static String getNativeVersion() { return "5.0.0"; }
    public static String getAPIChecksum() { return "0"; }
    public static boolean isSupported() { return false; }
    public static void setProtected(boolean protected_) {}
    public static boolean isProtected() { return false; }
    public static String getPrefix() { return ""; }
    public static void setCallbackThreadInitializer() {}
    public static long ffi_type_pointer = 0L;
    public static long ffi_type_void = 0L;
    public static long ffi_type_int = 0L;
    public static long ffi_type_uint32 = 0L;
    public static long ffi_type_sint32 = 0L;
    public static long ffi_type_double = 0L;
    public static long ffi_type_float = 0L;
    public static long ffi_type_sint8 = 0L;
    public static long ffi_type_uint8 = 0L;
    public static long ffi_type_sint16 = 0L;
    public static long ffi_type_uint16 = 0L;
    public static long ffi_type_uint64 = 0L;
    public static long ffi_type_sint64 = 0L;
    public static long ffi_type_longdouble = 0L;
    public static long ffi_type_pointer_size = 8;
    public static void main(String[] args) {}
    public static void dispose() {}
    public static Pointer getDirectByteBufferPointer(java.nio.ByteBuffer b) { return new Pointer(0L); }
    public static String toString(byte[] buf) { return new String(buf); }
    public static String toString(char[] buf) { return new String(buf); }
    public static Pointer getWindowPointer(java.awt.Component w) { return new Pointer(0L); }
    public static void setComponentPointer(java.awt.Component c, Pointer p) {}
    public static Library synchronizedLibrary(Library library) { return library; }
    public static interface Library {}
    public static interface Options {
        int OPTION_OPEN_FLAGS = 1;
        int OPTION_STRING_ENCODING = 2;
        int OPTION_STRUCTURE_ALIGNMENT = 3;
        int OPTION_TYPE_MAPPER = 4;
    }

    public static void register(Class<?> cls, NativeLibrary lib) {}
    public static void register(Class<?> cls) {}
}
