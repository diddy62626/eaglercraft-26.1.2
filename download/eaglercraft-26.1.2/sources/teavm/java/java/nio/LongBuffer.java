package java.nio;

public class LongBuffer extends Buffer implements Comparable<LongBuffer> {
    long[] backingArray;
    ByteBuffer byteBuffer; // non-null if this is a view of a ByteBuffer
    boolean readOnly;
    int offset;

    // Constructor for allocate/wrap
    LongBuffer(int capacity, long[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
        this.byteBuffer = null;
    }

    // Constructor for ByteBuffer view
    LongBuffer(ByteBuffer bb) {
        super(bb.remaining() / 8, bb.remaining() / 8, 0);
        this.byteBuffer = bb;
        this.backingArray = null;
        this.offset = 0;
        this.readOnly = bb.isReadOnly();
    }

    // ========== Factory methods ==========

    public static LongBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new LongBuffer(capacity, new long[capacity], 0, false);
    }

    public static LongBuffer wrap(long[] array) {
        return new LongBuffer(array.length, array, 0, false);
    }

    public static LongBuffer wrap(long[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        LongBuffer buf = new LongBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    // ========== Relative get/put ==========

    public long get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            long v = byteBuffer.getLong(byteBuffer.position + position * 8);
            position++;
            return v;
        }
        return backingArray[offset + position++];
    }

    public LongBuffer put(long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            byteBuffer.putLong(byteBuffer.position + position * 8, value);
            position++;
            return this;
        }
        backingArray[offset + position++] = value;
        return this;
    }

    // ========== Absolute get/put ==========

    public long get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            return byteBuffer.getLong(byteBuffer.position + index * 8);
        }
        return backingArray[offset + index];
    }

    public LongBuffer put(int index, long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            byteBuffer.putLong(byteBuffer.position + index * 8, value);
            return this;
        }
        backingArray[offset + index] = value;
        return this;
    }

    // ========== Bulk get/put ==========

    public LongBuffer get(long[] dst) {
        return get(dst, 0, dst.length);
    }

    public LongBuffer get(long[] dst, int off, int len) {
        if (off < 0 || len < 0 || off + len > dst.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len > remaining()) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < len; i++) {
                dst[off + i] = get();
            }
        } else {
            System.arraycopy(backingArray, offset + position, dst, off, len);
            position += len;
        }
        return this;
    }

    public LongBuffer put(long[] src) {
        return put(src, 0, src.length);
    }

    public LongBuffer put(long[] src, int off, int len) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (off < 0 || len < 0 || off + len > src.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len > remaining()) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < len; i++) {
                put(src[off + i]);
            }
        } else {
            System.arraycopy(src, off, backingArray, offset + position, len);
            position += len;
        }
        return this;
    }

    public LongBuffer put(LongBuffer src) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (src == this) {
            throw new IllegalArgumentException();
        }
        int srcRemaining = src.remaining();
        if (srcRemaining > remaining()) {
            throw new BufferOverflowException();
        }
        for (int i = 0; i < srcRemaining; i++) {
            put(src.get());
        }
        return this;
    }

    // ========== Slice / duplicate / asReadOnly ==========

    public LongBuffer slice() {
        if (byteBuffer != null) {
            ByteBuffer sliced = byteBuffer.slice();
            return new LongBuffer(sliced);
        }
        int rem = remaining();
        return new LongBuffer(rem, backingArray, offset + position, readOnly);
    }

    public LongBuffer duplicate() {
        if (byteBuffer != null) {
            LongBuffer buf = new LongBuffer(byteBuffer.duplicate());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        LongBuffer buf = new LongBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public LongBuffer asReadOnlyBuffer() {
        if (byteBuffer != null) {
            LongBuffer buf = new LongBuffer(byteBuffer.asReadOnlyBuffer());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        LongBuffer buf = new LongBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Array access ==========

    public final long[] array() {
        if (backingArray == null) {
            throw new UnsupportedOperationException();
        }
        return backingArray;
    }

    public final int arrayOffset() {
        return offset;
    }

    public boolean hasArray() {
        return !readOnly && backingArray != null;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isDirect() {
        return true;
    }

    // ========== Compact ==========

    public LongBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < remaining(); i++) {
                byteBuffer.putLong(byteBuffer.position + i * 8, byteBuffer.getLong(byteBuffer.position + position * 8 + i * 8));
            }
            position = remaining();
            limit = capacity;
            mark = -1;
            return this;
        }
        int rem = remaining();
        System.arraycopy(backingArray, offset + position, backingArray, offset, rem);
        position = rem;
        limit = capacity;
        mark = -1;
        return this;
    }

    // ========== Compare ==========

    public int compareTo(LongBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Long.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof LongBuffer)) {
            return false;
        }
        LongBuffer that = (LongBuffer) ob;
        if (this.remaining() != that.remaining()) {
            return false;
        }
        int n = this.remaining();
        for (int i = 0; i < n; i++) {
            if (this.get(this.position + i) != that.get(that.position + i)) {
                return false;
            }
        }
        return true;
    }
}
