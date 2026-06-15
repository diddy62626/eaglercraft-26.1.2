package java.nio;

public abstract class Buffer {
    int position;
    int limit;
    int capacity;
    int mark = -1;

    Buffer(int capacity, int limit, int position) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        this.capacity = capacity;
        this.limit = limit;
        this.position = position;
    }

    public final int capacity() {
        return capacity;
    }

    public final int position() {
        return position;
    }

    public Buffer position(int newPosition) {
        if (newPosition < 0 || newPosition > limit) {
            throw new IllegalArgumentException("position " + newPosition + " out of range [0," + limit + "]");
        }
        if (mark > newPosition) {
            mark = -1;
        }
        position = newPosition;
        return this;
    }

    public final int limit() {
        return limit;
    }

    public Buffer limit(int newLimit) {
        if (newLimit < 0 || newLimit > capacity) {
            throw new IllegalArgumentException("limit " + newLimit + " out of range [0," + capacity + "]");
        }
        if (position > newLimit) {
            position = newLimit;
        }
        if (mark > newLimit) {
            mark = -1;
        }
        limit = newLimit;
        return this;
    }

    public Buffer mark() {
        mark = position;
        return this;
    }

    public Buffer reset() {
        if (mark < 0) {
            throw new InvalidMarkException();
        }
        position = mark;
        return this;
    }

    public Buffer clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return this;
    }

    public Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }

    public Buffer rewind() {
        position = 0;
        mark = -1;
        return this;
    }

    public final int remaining() {
        return limit - position;
    }

    public final boolean hasRemaining() {
        return position < limit;
    }

    public abstract boolean isReadOnly();

    public abstract boolean hasArray();

    public abstract Object array();

    public abstract int arrayOffset();

    public boolean isDirect() {
        return true;
    }
}
