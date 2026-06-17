package java.nio;

/**
 * TeaVM stub for java.nio.ShortBuffer.
 * View over ByteBuffer's backing array with proper byte order.
 */
public class ShortBuffer extends Buffer implements Comparable<ShortBuffer> {

    final byte[] hb;
    final int offset;
    final ByteOrder order;
    private boolean isReadOnly;

    ShortBuffer(byte[] hb, int offset, int size, ByteOrder order) {
        super(-1, 0, size, size);
        this.hb = hb;
        this.offset = offset;
        this.order = order;
        this.isReadOnly = false;
    }

    public static ShortBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new ShortBuffer(new byte[capacity * 2], 0, capacity, ByteOrder.BIG_ENDIAN);
    }

    public static ShortBuffer wrap(short[] array) {
        return wrap(array, 0, array.length);
    }

    public static ShortBuffer wrap(short[] array, int offset, int length) {
        ShortBuffer buf = allocate(array.length);
        for (int i = offset; i < offset + length; i++)
            buf.put(array[i]);
        buf.position(offset);
        buf.limit(offset + length);
        return buf;
    }

    public short get() {
        return getShort(nextGetIndex());
    }

    public short get(int i) {
        return getShort(checkIndex(i));
    }

    public ShortBuffer get(short[] dst) {
        return get(dst, 0, dst.length);
    }

    public ShortBuffer get(short[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        for (int i = offset; i < offset + length; i++)
            dst[i] = get();
        return this;
    }

    public ShortBuffer put(short s) {
        putShort(nextPutIndex(), s);
        return this;
    }

    public ShortBuffer put(int i, short s) {
        putShort(checkIndex(i), s);
        return this;
    }

    public ShortBuffer put(ShortBuffer src) {
        if (src == this) throw new IllegalArgumentException();
        int n = src.remaining();
        if (n > remaining()) throw new BufferOverflowException();
        for (int i = 0; i < n; i++) put(src.get());
        return this;
    }

    public ShortBuffer put(short[] src) {
        return put(src, 0, src.length);
    }

    public ShortBuffer put(short[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining()) throw new BufferOverflowException();
        for (int i = offset; i < offset + length; i++) put(src[i]);
        return this;
    }

    @Override
    public ShortBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new ShortBuffer(hb, offset + pos * 2, rem, order);
    }

    @Override
    public ShortBuffer slice(int index, int length) {
        return new ShortBuffer(hb, offset + index * 2, length, order);
    }

    @Override
    public ShortBuffer duplicate() {
        ShortBuffer dup = new ShortBuffer(hb, offset, capacity, order);
        dup.position = this.position;
        dup.limit = this.limit;
        dup.mark = this.mark;
        return dup;
    }

    public ShortBuffer asReadOnlyBuffer() {
        ShortBuffer buf = duplicate();
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

    public ShortBuffer compact() {
        int pos = position;
        int rem = remaining();
        for (int i = 0; i < rem; i++)
            putShort(i, getShort(pos + i));
        position(rem);
        limit(capacity);
        mark = -1;
        return this;
    }

    @Override
    public int compareTo(ShortBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Short.compare(this.get(i), that.get(j));
            if (cmp != 0) return cmp;
        }
        return this.remaining() - that.remaining();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[pos=" + position() + " lim=" + limit() + " cap=" + capacity() + "]";
    }

    private short getShort(int i) {
        int bytePos = offset + i * 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((hb[bytePos] << 8) | (hb[bytePos + 1] & 0xFF));
        } else {
            return (short) ((hb[bytePos + 1] << 8) | (hb[bytePos] & 0xFF));
        }
    }

    private void putShort(int i, short v) {
        int bytePos = offset + i * 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[bytePos] = (byte) (v >> 8);
            hb[bytePos + 1] = (byte) v;
        } else {
            hb[bytePos] = (byte) v;
            hb[bytePos + 1] = (byte) (v >> 8);
        }
    }

    static void checkBounds(int off, int len, int arrayLength) {
        if ((off | len | (off + len) | (arrayLength - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }
}
