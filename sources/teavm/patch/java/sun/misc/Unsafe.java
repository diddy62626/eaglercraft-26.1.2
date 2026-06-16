package sun.misc;

/**
 * TeaVM stub for sun.misc.Unsafe.
 * Provides minimal no-op / stub implementations of dangerous operations.
 * TeaVM does not actually support real Unsafe operations; this stub exists
 * only so classes that REFERENCE Unsafe compile (the methods are never called
 * at runtime in browser).
 */
public class Unsafe {
    private static final Unsafe UNSAFE = new Unsafe();

    public static Unsafe getUnsafe() { return UNSAFE; }

    public long getAddress(Object o, long offset) { return 0; }
    public void putAddress(Object o, long offset, long x) {}
    public int getInt(Object o, long offset) { return 0; }
    public void putInt(Object o, long offset, int x) {}
    public long getLong(Object o, long offset) { return 0L; }
    public void putLong(Object o, long offset, long x) {}
    public Object getObject(Object o, long offset) { return null; }
    public void putObject(Object o, long offset, Object x) {}
    public boolean getBoolean(Object o, long offset) { return false; }
    public void putBoolean(Object o, long offset, boolean x) {}
    public byte getByte(Object o, long offset) { return 0; }
    public void putByte(Object o, long offset, byte x) {}
    public short getShort(Object o, long offset) { return 0; }
    public void putShort(Object o, long offset, short x) {}
    public char getChar(Object o, long offset) { return 0; }
    public void putChar(Object o, long offset, char x) {}
    public float getFloat(Object o, long offset) { return 0f; }
    public void putFloat(Object o, long offset, float x) {}
    public double getDouble(Object o, long offset) { return 0.0; }
    public void putDouble(Object o, long offset, double x) {}

    public int arrayBaseOffset(Class<?> arrayClass) { return 0; }
    public int arrayIndexScale(Class<?> arrayClass) { return 1; }
    public int addressSize() { return 8; }
    public int pageSize() { return 4096; }

    public long objectFieldOffset(java.lang.reflect.Field f) { return 0L; }
    public long staticFieldOffset(java.lang.reflect.Field f) { return 0L; }
    public Object staticFieldBase(java.lang.reflect.Field f) { return null; }

    public boolean compareAndSwapInt(Object o, long offset, int expected, int x) { return false; }
    public boolean compareAndSwapLong(Object o, long offset, long expected, long x) { return false; }
    public boolean compareAndSwapObject(Object o, long offset, Object expected, Object x) { return false; }

    public void park(boolean isAbsolute, long time) {}
    public void unpark(Thread thread) {}

    public Object allocateInstance(Class<?> cls) throws java.lang.InstantiationException {
        throw new java.lang.InstantiationException("Unsafe.allocateInstance not supported");
    }

    public long allocateMemory(long bytes) { return 0L; }
    public void freeMemory(long address) {}
    public long reallocateMemory(long address, long bytes) { return 0L; }
    public void setMemory(long address, long bytes, byte value) {}
    public void copyMemory(long srcAddress, long destAddress, long bytes) {}
    public void copyMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes) {}

    public Object getAndSetObject(Object o, long offset, Object newValue) {
        Object old = getObject(o, offset);
        putObject(o, offset, newValue);
        return old;
    }
    public int getAndSetInt(Object o, long offset, int newValue) {
        int old = getInt(o, offset);
        putInt(o, offset, newValue);
        return old;
    }
    public long getAndSetLong(Object o, long offset, long newValue) {
        long old = getLong(o, offset);
        putLong(o, offset, newValue);
        return old;
    }
    public int getAndAddInt(Object o, long offset, int delta) {
        int old = getInt(o, offset);
        putInt(o, offset, old + delta);
        return old;
    }
    public long getAndAddLong(Object o, long offset, long delta) {
        long old = getLong(o, offset);
        putLong(o, offset, old + delta);
        return old;
    }


    // ========== MC 26.1.2 additions ==========

    public native Object getObjectVolatile(Object obj, long offset);
    public native void putOrderedLong(Object obj, long offset, long value);
    public native void putOrderedObject(Object obj, long offset, Object value);
}
