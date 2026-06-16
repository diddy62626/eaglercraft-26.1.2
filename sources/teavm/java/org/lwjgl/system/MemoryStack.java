package org.lwjgl.system;

import java.nio.*;

public class MemoryStack {
    private static final MemoryStack INSTANCE = new MemoryStack();

    public static MemoryStack stackPush() {
        return INSTANCE;
    }

    public MemoryStack pop() {
        return INSTANCE;
    }

    public static MemoryStack create() {
        return INSTANCE;
    }

    public static MemoryStack get() {
        return INSTANCE;
    }

    // ========== Allocate temporary buffers ==========

    public IntBuffer mallocInt(int size) {
        return IntBuffer.allocate(size);
    }

    public FloatBuffer mallocFloat(int size) {
        return FloatBuffer.allocate(size);
    }

    public LongBuffer mallocLong(int size) {
        return LongBuffer.allocate(size);
    }

    public ByteBuffer malloc(int size) {
        return ByteBuffer.allocate(size);
    }

    public ShortBuffer mallocShort(int size) {
        return ShortBuffer.allocate(size);
    }

    public DoubleBuffer mallocDouble(int size) {
        return DoubleBuffer.allocate(size);
    }

    public CharBuffer mallocChar(int size) {
        return CharBuffer.allocate(size);
    }

    // ========== Pointer sizing ==========

    public int getPointerSize() {
        return 4; // 32-bit for WebGL
    }

    // ========== Stack-relative pointers ==========

    public long getPointer() {
        return 0L;
    }

    public void setPointer(long pointer) {
        // no-op
    }

    // ========== Convenience methods ==========

    public IntBuffer ints(int... values) {
        IntBuffer buf = IntBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    public FloatBuffer floats(float... values) {
        FloatBuffer buf = FloatBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    public LongBuffer longs(long... values) {
        LongBuffer buf = LongBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    public DoubleBuffer doubles(double... values) {
        DoubleBuffer buf = DoubleBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    public ByteBuffer bytes(byte... values) {
        ByteBuffer buf = ByteBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    public ShortBuffer shorts(short... values) {
        ShortBuffer buf = ShortBuffer.allocate(values.length);
        buf.put(values);
        buf.flip();
        return buf;
    }

    // ========== Context check ==========

    public static boolean isStackThread() {
        return true;
    }

    // ========== Closeable support ==========

    public void close() {
        // no-op
    }

    /**
     * MC 26.1.2: Allocates a PointerBuffer of the given size on this stack.
     */
    public org.lwjgl.PointerBuffer mallocPointer(int size) {
        return new org.lwjgl.PointerBuffer(java.nio.ByteBuffer.allocateDirect(size * 8));
    }

    public java.nio.IntBuffer callocInt(int size) {
        return java.nio.ByteBuffer.allocateDirect(size * 4).order(java.nio.ByteOrder.nativeOrder()).asIntBuffer();
    }
}
