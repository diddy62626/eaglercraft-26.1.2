package org.lwjgl.system;

import java.nio.ByteBuffer;

/**
 * TeaVM-compatible stub for LWJGL's CustomBuffer.
 * In the browser, native memory operations are replaced with JS typed arrays.
 */
public abstract class CustomBuffer<T extends CustomBuffer<T>> {

    protected long address;
    protected int position;
    protected int limit;
    protected int capacity;
    protected ByteBuffer container;

    protected CustomBuffer(long address, int capacity) {
        this.address = address;
        this.capacity = capacity;
        this.limit = capacity;
        this.position = 0;
    }

    protected CustomBuffer(ByteBuffer container, int capacity) {
        this.container = container;
        this.capacity = capacity;
        this.limit = capacity;
        this.position = 0;
        this.address = 0L;
    }

    public final int capacity() {
        return capacity;
    }

    public final int position() {
        return position;
    }

    public final T position(int newPosition) {
        this.position = newPosition;
        @SuppressWarnings("unchecked")
        T self = (T) this;
        return self;
    }

    public final int limit() {
        return limit;
    }

    public final T limit(int newLimit) {
        this.limit = newLimit;
        @SuppressWarnings("unchecked")
        T self = (T) this;
        return self;
    }

    public final int remaining() {
        return limit - position;
    }

    public final boolean hasRemaining() {
        return position < limit;
    }

    public final T flip() {
        limit = position;
        position = 0;
        @SuppressWarnings("unchecked")
        T self = (T) this;
        return self;
    }

    public final T clear() {
        position = 0;
        limit = capacity;
        @SuppressWarnings("unchecked")
        T self = (T) this;
        return self;
    }

    public final T rewind() {
        position = 0;
        @SuppressWarnings("unchecked")
        T self = (T) this;
        return self;
    }

    public final long address() {
        return address;
    }

    public final ByteBuffer getContainer() {
        return container;
    }
}
