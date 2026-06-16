package java.nio;

/**
 * TeaVM stub for java.nio.FloatBuffer.
 * View over ByteBuffer's backing array with proper byte order.
 * CRITICAL for MC rendering - used for vertex positions, UV coords, normals, etc.
 */
public class FloatBuffer extends Buffer implements Comparable<FloatBuffer> {

    final byte[] hb;
    final int offset;
    final ByteOrder order;
    private boolean isReadOnly;

    FloatBuffer(byte[] hb, int offset, int size, ByteOrder order) {
        super(-1, 0, size, size);
        this.hb = hb;
        this.offset = offset;
        this.order = order;
        this.isReadOnly = false;
    }

    public static FloatBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new FloatBuffer(new byte[capacity * 4], 0, capacity, ByteOrder.BIG_ENDIAN);
    }

    public static FloatBuffer wrap(float[] array) {
        return wrap(array, 0, array.length);
    }

    public static FloatBuffer wrap(float[] array, int offset, int length) {
        FloatBuffer buf = allocate(array.length);
        for (int i = offset; i < offset + length; i++)
            buf.put(array[i]);
        buf.position(offset);
        buf.limit(offset + length);
        return buf;
    }

    public float get() {
        return Float.intBitsToFloat(getInt(nextGetIndex()));
    }

    public float get(int i) {
        return Float.intBitsToFloat(getInt(checkIndex(i)));
    }

    public FloatBuffer get(float[] dst) {
        return get(dst, 0, dst.length);
    }

    public FloatBuffer get(float[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        for (int i = offset; i < offset + length; i++)
            dst[i] = get();
        return this;
    }

    public FloatBuffer put(float v) {
        putInt(nextPutIndex(), Float.floatToRawIntBits(v));
        return this;
    }

    public FloatBuffer put(int i, float v) {
        putInt(checkIndex(i), Float.floatToRawIntBits(v));
        return this;
    }

    public FloatBuffer put(FloatBuffer src) {
        if (src == this) throw new IllegalArgumentException();
        int n = src.remaining();
        if (n > remaining()) throw new BufferOverflowException();
        for (int i = 0; i < n; i++) put(src.get());
        return this;
    }

    public FloatBuffer put(float[] src) {
        return put(src, 0, src.length);
    }

    public FloatBuffer put(float[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining()) throw new BufferOverflowException();
        for (int i = offset; i < offset + length; i++) put(src[i]);
        return this;
    }

    @Override
    public FloatBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new FloatBuffer(hb, offset + pos * 4, rem, order);
    }

    @Override
    public FloatBuffer slice(int index, int length) {
        return new FloatBuffer(hb, offset + index * 4, length, order);
    }

    @Override
    public FloatBuffer duplicate() {
        FloatBuffer dup = new FloatBuffer(hb, offset, capacity, order);
        dup.position = this.position;
        dup.limit = this.limit;
        dup.mark = this.mark;
        return dup;
    }

    public FloatBuffer asReadOnlyBuffer() {
        FloatBuffer buf = duplicate();
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

    public FloatBuffer compact() {
        int pos = position;
        int rem = remaining();
        for (int i = 0; i < rem; i++) {
            int srcBytePos = offset + (pos + i) * 4;
            int dstBytePos = offset + i * 4;
            System.arraycopy(hb, srcBytePos, hb, dstBytePos, 4);
        }
        position(rem);
        limit(capacity);
        mark = -1;
        return this;
    }

    @Override
    public int compareTo(FloatBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Float.compare(this.get(i), that.get(j));
            if (cmp != 0) return cmp;
        }
        return this.remaining() - that.remaining();
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof FloatBuffer)) return false;
        FloatBuffer that = (FloatBuffer) ob;
        if (this.remaining() != that.remaining()) return false;
        int p = this.position();
        for (int i = this.limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
            if (Float.compare(this.get(i), that.get(j)) != 0) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        int p = position();
        for (int i = limit() - 1; i >= p; i--)
            h = 31 * h + Float.floatToRawIntBits(get(i));
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
