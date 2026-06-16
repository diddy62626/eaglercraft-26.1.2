package java.nio;

/**
 * TeaVM stub for java.nio.IntBuffer.
 * View over ByteBuffer's backing array with proper byte order.
 * CRITICAL for MC rendering - used for vertex data, color data, etc.
 */
public class IntBuffer extends Buffer implements Comparable<IntBuffer> {

    final byte[] hb;
    final int offset;
    final ByteOrder order;
    private boolean isReadOnly;

    IntBuffer(byte[] hb, int offset, int size, ByteOrder order) {
        super(-1, 0, size, size);
        this.hb = hb;
        this.offset = offset;
        this.order = order;
        this.isReadOnly = false;
    }

    public static IntBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new IntBuffer(new byte[capacity * 4], 0, capacity, ByteOrder.BIG_ENDIAN);
    }

    public static IntBuffer wrap(int[] array) {
        return wrap(array, 0, array.length);
    }

    public static IntBuffer wrap(int[] array, int offset, int length) {
        IntBuffer buf = allocate(array.length);
        for (int i = offset; i < offset + length; i++)
            buf.put(array[i]);
        buf.position(offset);
        buf.limit(offset + length);
        return buf;
    }

    public int get() {
        return getInt(nextGetIndex());
    }

    public int get(int i) {
        return getInt(checkIndex(i));
    }

    public IntBuffer get(int[] dst) {
        return get(dst, 0, dst.length);
    }

    public IntBuffer get(int[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        for (int i = offset; i < offset + length; i++)
            dst[i] = get();
        return this;
    }

    public IntBuffer put(int v) {
        putInt(nextPutIndex(), v);
        return this;
    }

    public IntBuffer put(int i, int v) {
        putInt(checkIndex(i), v);
        return this;
    }

    public IntBuffer put(IntBuffer src) {
        if (src == this) throw new IllegalArgumentException();
        int n = src.remaining();
        if (n > remaining()) throw new BufferOverflowException();
        for (int i = 0; i < n; i++) put(src.get());
        return this;
    }

    public IntBuffer put(int[] src) {
        return put(src, 0, src.length);
    }

    public IntBuffer put(int[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining()) throw new BufferOverflowException();
        for (int i = offset; i < offset + length; i++) put(src[i]);
        return this;
    }

    @Override
    public IntBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new IntBuffer(hb, offset + pos * 4, rem, order);
    }

    @Override
    public IntBuffer slice(int index, int length) {
        return new IntBuffer(hb, offset + index * 4, length, order);
    }

    @Override
    public IntBuffer duplicate() {
        IntBuffer dup = new IntBuffer(hb, offset, capacity, order);
        dup.position = this.position;
        dup.limit = this.limit;
        dup.mark = this.mark;
        return dup;
    }

    public IntBuffer asReadOnlyBuffer() {
        IntBuffer buf = duplicate();
        buf.isReadOnly = true;
        return buf;
    }

    @Override
    public boolean isReadOnly() { return isReadOnly; }

    @Override
    public boolean hasArray() { return false; }

    @Override
    public Object array() { throw new UnsupportedOperationException(); }

    @Override
    public int arrayOffset() { throw new UnsupportedOperationException(); }

    @Override
    public boolean isDirect() { return false; }

    public ByteOrder order() { return order; }

    public IntBuffer compact() {
        int pos = position;
        int rem = remaining();
        for (int i = 0; i < rem; i++)
            putInt(i, getInt(pos + i));
        position(rem);
        limit(capacity);
        mark = -1;
        return this;
    }

    @Override
    public int compareTo(IntBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Integer.compare(this.get(i), that.get(j));
            if (cmp != 0) return cmp;
        }
        return this.remaining() - that.remaining();
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof IntBuffer)) return false;
        IntBuffer that = (IntBuffer) ob;
        if (this.remaining() != that.remaining()) return false;
        int p = this.position();
        for (int i = this.limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
            if (this.get(i) != that.get(j)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        int p = position();
        for (int i = limit() - 1; i >= p; i--)
            h = 31 * h + get(i);
        return h;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[pos=" + position() + " lim=" + limit() + " cap=" + capacity() + "]";
    }

    private int getInt(int i) {
        int bytePos = offset + i * 4;
        if (order == ByteOrder.BIG_ENDIAN) {
            return ((hb[bytePos] & 0xFF) << 24) |
                   ((hb[bytePos + 1] & 0xFF) << 16) |
                   ((hb[bytePos + 2] & 0xFF) << 8) |
                   (hb[bytePos + 3] & 0xFF);
        } else {
            return ((hb[bytePos + 3] & 0xFF) << 24) |
                   ((hb[bytePos + 2] & 0xFF) << 16) |
                   ((hb[bytePos + 1] & 0xFF) << 8) |
                   (hb[bytePos] & 0xFF);
        }
    }

    private void putInt(int i, int v) {
        int bytePos = offset + i * 4;
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[bytePos] = (byte) (v >> 24);
            hb[bytePos + 1] = (byte) (v >> 16);
            hb[bytePos + 2] = (byte) (v >> 8);
            hb[bytePos + 3] = (byte) v;
        } else {
            hb[bytePos] = (byte) v;
            hb[bytePos + 1] = (byte) (v >> 8);
            hb[bytePos + 2] = (byte) (v >> 16);
            hb[bytePos + 3] = (byte) (v >> 24);
        }
    }

    static void checkBounds(int off, int len, int arrayLength) {
        if ((off | len | (off + len) | (arrayLength - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }
}
