package java.nio;

/**
 * TeaVM stub for java.nio.Buffer.
 * Base class for all typed buffer implementations.
 * Uses simple array-based backing with no native memory.
 */
public abstract class Buffer {

    // Invariants: mark <= position <= limit <= capacity
    int mark = -1;
    int position = 0;
    int limit;
    int capacity;
    long address;

    Buffer(int mark, int pos, int lim, int cap) {
        if (cap < 0)
            throw new IllegalArgumentException("Negative capacity: " + cap);
        this.capacity = cap;
        this.limit = lim;
        this.position = pos;
        if (mark >= 0) {
            if (mark > pos)
                throw new IllegalArgumentException("mark > position: (" + mark + " > " + pos + ")");
            this.mark = mark;
        }
    }

    public final int capacity() {
        return capacity;
    }

    public final int position() {
        return position;
    }

    public Buffer position(int newPosition) {
        if ((newPosition > limit) || (newPosition < 0))
            throw new IllegalArgumentException();
        if (mark > newPosition) mark = -1;
        position = newPosition;
        return this;
    }

    public final int limit() {
        return limit;
    }

    public Buffer limit(int newLimit) {
        if ((newLimit > capacity) || (newLimit < 0))
            throw new IllegalArgumentException();
        if (mark > newLimit) mark = -1;
        if (position > newLimit) position = newLimit;
        limit = newLimit;
        return this;
    }

    public Buffer mark() {
        mark = position;
        return this;
    }

    public Buffer reset() {
        int m = mark;
        if (m < 0)
            throw new InvalidMarkException();
        position = m;
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

    public boolean hasArray() {
        return false;
    }

    public Object array() {
        throw new UnsupportedOperationException();
    }

    public int arrayOffset() {
        throw new UnsupportedOperationException();
    }

    public boolean isDirect() {
        return false;
    }

    final int nextGetIndex() {
        if (position >= limit)
            throw new BufferUnderflowException();
        return position++;
    }

    final int nextGetIndex(int nb) {
        if (limit - position < nb)
            throw new BufferUnderflowException();
        int p = position;
        position += nb;
        return p;
    }

    final int nextPutIndex() {
        if (position >= limit)
            throw new BufferOverflowException();
        return position++;
    }

    final int nextPutIndex(int nb) {
        if (limit - position < nb)
            throw new BufferOverflowException();
        int p = position;
        position += nb;
        return p;
    }

    final int checkIndex(int i) {
        if ((i < 0) || (i >= limit))
            throw new IndexOutOfBoundsException("index=" + i + ", limit=" + limit);
        return i;
    }

    final int checkIndex(int i, int nb) {
        if ((i < 0) || (nb > limit - i))
            throw new IndexOutOfBoundsException("index=" + i + ", limit=" + limit);
        return i;
    }

    // Abstract methods implemented by typed buffer subclasses
    public abstract Buffer slice();

    public abstract Buffer slice(int index, int length);

    public abstract Buffer duplicate();
}
