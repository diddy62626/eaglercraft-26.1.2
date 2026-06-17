package org.lwjgl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.DoubleBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.CharBuffer;

public final class BufferUtils {
    public static ByteBuffer createByteBuffer(int size) { return ByteBuffer.allocateDirect(size); }
    public static ShortBuffer createShortBuffer(int size) { return ShortBuffer.allocate(size); }
    public static CharBuffer createCharBuffer(int size) { return CharBuffer.allocate(size); }
    public static IntBuffer createIntBuffer(int size) { return IntBuffer.allocate(size); }
    public static LongBuffer createLongBuffer(int size) { return LongBuffer.allocate(size); }
    public static FloatBuffer createFloatBuffer(int size) { return FloatBuffer.allocate(size); }
    public static DoubleBuffer createDoubleBuffer(int size) { return DoubleBuffer.allocate(size); }
}
