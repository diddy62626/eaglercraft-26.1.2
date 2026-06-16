package org.lwjgl;

import java.nio.ByteBuffer;

/**
 * TeaVM-compatible stub for LWJGL's PointerBuffer.
 * On 64-bit JVM, pointers are 8 bytes; in TeaVM/browser we store as int offsets.
 */
public class PointerBuffer {

    private final ByteBuffer backing;
    private final int capacity;

    public PointerBuffer(ByteBuffer backing) {
        this.backing = backing;
        this.capacity = backing != null ? backing.capacity() / 8 : 0;
    }

    public PointerBuffer(long address, int capacity) {
        this.backing = null;
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    public long get(int index) {
        return 0L;
    }

    public PointerBuffer put(int index, long value) {
        return this;
    }

    public long get() {
        return 0L;
    }

    public PointerBuffer put(long value) {
        return this;
    }

    public PointerBuffer flip() {
        return this;
    }

    public PointerBuffer clear() {
        return this;
    }

    public int remaining() {
        return capacity;
    }

    public boolean hasRemaining() {
        return capacity > 0;
    }

    public int position() {
        return 0;
    }

    public PointerBuffer position(int pos) {
        return this;
    }

    public PointerBuffer limit(int limit) {
        return this;
    }

    public int limit() {
        return capacity;
    }

    public static PointerBuffer allocate(int capacity) {
        return new PointerBuffer(0L, capacity);
    }

    public static PointerBuffer create(long address, int capacity) {
        return new PointerBuffer(address, capacity);
    }
}
