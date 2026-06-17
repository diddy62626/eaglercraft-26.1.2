package java.nio;

/**
 * TeaVM stub for java.nio.DoubleBuffer.
 * View over ByteBuffer's backing array with proper byte order.
 */
public class DoubleBuffer extends Buffer implements Comparable<DoubleBuffer> {

    final byte[] hb;
    final int offset;
    final ByteOrder order;
    private boolean isReadOnly;

    DoubleBuffer(byte[] hb, int offset, int size, ByteOrder order) {
        super(-1, 0, size, size);
        this.hb = hb;
        this.offset = offset;
        this.order = order;
        this.isReadOnly = false;
    }

    public static DoubleBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new DoubleBuffer(new byte[capacity * 8], 0, capacity, ByteOrder.BIG_ENDIAN);
    }

    public static DoubleBuffer wrap(double[] array) {
        return wrap(array, 0, array.length);
    }

    public static DoubleBuffer wrap(double[] array, int offset, int length) {
        DoubleBuffer buf = allocate(array.length);
        for (int i = offset; i < offset + length; i++)
            buf.put(array[i]);
        buf.position(offset);
        buf.limit(offset + length);
        return buf;
    }

    public double get() {
        return Double.longBitsToDouble(getLong(nextGetIndex()));
    }

    public double get(int i) {
        return Double.longBitsToDouble(getLong(checkIndex(i)));
    }

    public DoubleBuffer get(double[] dst) {
        return get(dst, 0, dst.length);
    }

    public DoubleBuffer get(double[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        for (int i = offset; i < offset + length; i++)
            dst[i] = get();
        return this;
    }

    public DoubleBuffer put(double v) {
        putLong(nextPutIndex(), Double.doubleToRawLongBits(v));
        return this;
    }

    public DoubleBuffer put(int i, double v) {
        putLong(checkIndex(i), Double.doubleToRawLongBits(v));
        return this;
    }

    public DoubleBuffer put(DoubleBuffer src) {
        if (src == this) throw new IllegalArgumentException();
        int n = src.remaining();
        if (n > remaining()) throw new BufferOverflowException();
        for (int i = 0; i < n; i++) put(src.get());
        return this;
    }

    public DoubleBuffer put(double[] src) {
        return put(src, 0, src.length);
    }

    public DoubleBuffer put(double[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining()) throw new BufferOverflowException();
        for (int i = offset; i < offset + length; i++) put(src[i]);
        return this;
    }

    @Override
    public DoubleBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new DoubleBuffer(hb, offset + pos * 8, rem, order);
    }

    @Override
    public DoubleBuffer slice(int index, int length) {
        return new DoubleBuffer(hb, offset + index * 8, length, order);
    }

    @Override
    public DoubleBuffer duplicate() {
        DoubleBuffer dup = new DoubleBuffer(hb, offset, capacity, order);
        dup.position = this.position;
        dup.limit = this.limit;
        dup.mark = this.mark;
        return dup;
    }

    public DoubleBuffer asReadOnlyBuffer() {
        DoubleBuffer buf = duplicate();
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

    public DoubleBuffer compact() {
        int pos = position;
        int rem = remaining();
        for (int i = 0; i < rem; i++) {
            int srcBytePos = offset + (pos + i) * 8;
            int dstBytePos = offset + i * 8;
            System.arraycopy(hb, srcBytePos, hb, dstBytePos, 8);
        }
        position(rem);
        limit(capacity);
        mark = -1;
        return this;
    }

    @Override
    public int compareTo(DoubleBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Double.compare(this.get(i), that.get(j));
            if (cmp != 0) return cmp;
        }
        return this.remaining() - that.remaining();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[pos=" + position() + " lim=" + limit() + " cap=" + capacity() + "]";
    }

    private long getLong(int i) {
        int bytePos = offset + i * 8;
        if (order == ByteOrder.BIG_ENDIAN) {
            return ((long) hb[bytePos] << 56) |
                   ((long) (hb[bytePos + 1] & 0xFF) << 48) |
                   ((long) (hb[bytePos + 2] & 0xFF) << 40) |
                   ((long) (hb[bytePos + 3] & 0xFF) << 32) |
                   ((long) (hb[bytePos + 4] & 0xFF) << 24) |
                   ((long) (hb[bytePos + 5] & 0xFF) << 16) |
                   ((long) (hb[bytePos + 6] & 0xFF) << 8) |
                   ((long) (hb[bytePos + 7] & 0xFF));
        } else {
            return ((long) hb[bytePos + 7] << 56) |
                   ((long) (hb[bytePos + 6] & 0xFF) << 48) |
                   ((long) (hb[bytePos + 5] & 0xFF) << 40) |
                   ((long) (hb[bytePos + 4] & 0xFF) << 32) |
                   ((long) (hb[bytePos + 3] & 0xFF) << 24) |
                   ((long) (hb[bytePos + 2] & 0xFF) << 16) |
                   ((long) (hb[bytePos + 1] & 0xFF) << 8) |
                   ((long) (hb[bytePos] & 0xFF));
        }
    }

    private void putLong(int i, long v) {
        int bytePos = offset + i * 8;
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[bytePos] = (byte) (v >> 56);
            hb[bytePos + 1] = (byte) (v >> 48);
            hb[bytePos + 2] = (byte) (v >> 40);
            hb[bytePos + 3] = (byte) (v >> 32);
            hb[bytePos + 4] = (byte) (v >> 24);
            hb[bytePos + 5] = (byte) (v >> 16);
            hb[bytePos + 6] = (byte) (v >> 8);
            hb[bytePos + 7] = (byte) v;
        } else {
            hb[bytePos] = (byte) v;
            hb[bytePos + 1] = (byte) (v >> 8);
            hb[bytePos + 2] = (byte) (v >> 16);
            hb[bytePos + 3] = (byte) (v >> 24);
            hb[bytePos + 4] = (byte) (v >> 32);
            hb[bytePos + 5] = (byte) (v >> 40);
            hb[bytePos + 6] = (byte) (v >> 48);
            hb[bytePos + 7] = (byte) (v >> 56);
        }
    }

    static void checkBounds(int off, int len, int arrayLength) {
        if ((off | len | (off + len) | (arrayLength - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }
}
