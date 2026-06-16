package java.nio;

/**
 * TeaVM stub for java.nio.ByteBuffer.
 * CRITICAL: MC uses ByteBuffers extensively for rendering.
 * This uses a byte[] backing array with no native memory.
 * All byte order operations default to BIG_ENDIAN (network byte order).
 */
public class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {

    final byte[] hb;
    final int offset;
    boolean isReadOnly;
    ByteOrder order = ByteOrder.BIG_ENDIAN;

    ByteBuffer(int mark, int pos, int lim, int cap, byte[] hb, int offset) {
        super(mark, pos, lim, cap);
        this.hb = hb;
        this.offset = offset;
        this.isReadOnly = false;
    }

    ByteBuffer(int mark, int pos, int lim, int cap) {
        this(mark, pos, lim, cap, null, 0);
    }

    // -- Factory methods --

    public static ByteBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new ByteBuffer(-1, 0, capacity, capacity, new byte[capacity], 0);
    }

    public static ByteBuffer allocateDirect(int capacity) {
        // In browser, allocateDirect is same as allocate (no native memory)
        return allocate(capacity);
    }

    public static ByteBuffer wrap(byte[] array) {
        return wrap(array, 0, array.length);
    }

    public static ByteBuffer wrap(byte[] array, int offset, int length) {
        try {
            return new ByteBuffer(-1, offset, offset + length, array.length, array, 0);
        } catch (IllegalArgumentException x) {
            throw new IndexOutOfBoundsException();
        }
    }

    // -- Raw get/put --

    public ByteBuffer get(byte[] dst) {
        return get(dst, 0, dst.length);
    }

    public ByteBuffer get(byte[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        int end = offset + length;
        for (int i = offset; i < end; i++)
            dst[i] = get();
        return this;
    }

    public byte get() {
        return hb[ix(nextGetIndex())];
    }

    public byte get(int i) {
        return hb[ix(checkIndex(i))];
    }

    public ByteBuffer put(byte b) {
        hb[ix(nextPutIndex())] = b;
        return this;
    }

    public ByteBuffer put(int i, byte b) {
        hb[ix(checkIndex(i))] = b;
        return this;
    }

    public ByteBuffer put(ByteBuffer src) {
        if (src == this)
            throw new IllegalArgumentException();
        if (isReadOnly)
            throw new ReadOnlyBufferException();
        int n = src.remaining();
        if (n > remaining())
            throw new BufferOverflowException();
        for (int i = 0; i < n; i++)
            put(src.get());
        return this;
    }

    public ByteBuffer put(byte[] src) {
        return put(src, 0, src.length);
    }

    public ByteBuffer put(byte[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining())
            throw new BufferOverflowException();
        int end = offset + length;
        for (int i = offset; i < end; i++)
            this.put(src[i]);
        return this;
    }

    // -- Typed get/put: char --

    public char getChar() {
        return _getChar(nextGetIndex(2));
    }

    public char getChar(int i) {
        return _getChar(checkIndex(i, 2));
    }

    public ByteBuffer putChar(char value) {
        _putChar(nextPutIndex(2), value);
        return this;
    }

    public ByteBuffer putChar(int i, char value) {
        _putChar(checkIndex(i, 2), value);
        return this;
    }

    // -- Typed get/put: short --

    public short getShort() {
        return _getShort(nextGetIndex(2));
    }

    public short getShort(int i) {
        return _getShort(checkIndex(i, 2));
    }

    public ByteBuffer putShort(short value) {
        _putShort(nextPutIndex(2), value);
        return this;
    }

    public ByteBuffer putShort(int i, short value) {
        _putShort(checkIndex(i, 2), value);
        return this;
    }

    // -- Typed get/put: int --

    public int getInt() {
        return _getInt(nextGetIndex(4));
    }

    public int getInt(int i) {
        return _getInt(checkIndex(i, 4));
    }

    public ByteBuffer putInt(int value) {
        _putInt(nextPutIndex(4), value);
        return this;
    }

    public ByteBuffer putInt(int i, int value) {
        _putInt(checkIndex(i, 4), value);
        return this;
    }

    // -- Typed get/put: long --

    public long getLong() {
        return _getLong(nextGetIndex(8));
    }

    public long getLong(int i) {
        return _getLong(checkIndex(i, 8));
    }

    public ByteBuffer putLong(long value) {
        _putLong(nextPutIndex(8), value);
        return this;
    }

    public ByteBuffer putLong(int i, long value) {
        _putLong(checkIndex(i, 8), value);
        return this;
    }

    // -- Typed get/put: float --

    public float getFloat() {
        return Float.intBitsToFloat(_getInt(nextGetIndex(4)));
    }

    public float getFloat(int i) {
        return Float.intBitsToFloat(_getInt(checkIndex(i, 4)));
    }

    public ByteBuffer putFloat(float value) {
        _putInt(nextPutIndex(4), Float.floatToRawIntBits(value));
        return this;
    }

    public ByteBuffer putFloat(int i, float value) {
        _putInt(checkIndex(i, 4), Float.floatToRawIntBits(value));
        return this;
    }

    // -- Typed get/put: double --

    public double getDouble() {
        return Double.longBitsToDouble(_getLong(nextGetIndex(8)));
    }

    public double getDouble(int i) {
        return Double.longBitsToDouble(_getLong(checkIndex(i, 8)));
    }

    public ByteBuffer putDouble(double value) {
        _putLong(nextPutIndex(8), Double.doubleToRawLongBits(value));
        return this;
    }

    public ByteBuffer putDouble(int i, double value) {
        _putLong(checkIndex(i, 8), Double.doubleToRawLongBits(value));
        return this;
    }

    // -- Private byte-order-aware read/write helpers --

    private int ix(int i) {
        return i + offset;
    }

    private byte _get(int i) {
        return hb[ix(i)];
    }

    private void _put(int i, byte b) {
        hb[ix(i)] = b;
    }

    private char _getChar(int i) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return (char) ((hb[ix(i)] << 8) | (hb[ix(i + 1)] & 0xFF));
        } else {
            return (char) ((hb[ix(i + 1)] << 8) | (hb[ix(i)] & 0xFF));
        }
    }

    private void _putChar(int i, char v) {
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[ix(i)] = (byte) (v >> 8);
            hb[ix(i + 1)] = (byte) v;
        } else {
            hb[ix(i)] = (byte) v;
            hb[ix(i + 1)] = (byte) (v >> 8);
        }
    }

    private short _getShort(int i) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return (short) ((hb[ix(i)] << 8) | (hb[ix(i + 1)] & 0xFF));
        } else {
            return (short) ((hb[ix(i + 1)] << 8) | (hb[ix(i)] & 0xFF));
        }
    }

    private void _putShort(int i, short v) {
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[ix(i)] = (byte) (v >> 8);
            hb[ix(i + 1)] = (byte) v;
        } else {
            hb[ix(i)] = (byte) v;
            hb[ix(i + 1)] = (byte) (v >> 8);
        }
    }

    private int _getInt(int i) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return ((hb[ix(i)] & 0xFF) << 24) |
                   ((hb[ix(i + 1)] & 0xFF) << 16) |
                   ((hb[ix(i + 2)] & 0xFF) << 8) |
                   (hb[ix(i + 3)] & 0xFF);
        } else {
            return ((hb[ix(i + 3)] & 0xFF) << 24) |
                   ((hb[ix(i + 2)] & 0xFF) << 16) |
                   ((hb[ix(i + 1)] & 0xFF) << 8) |
                   (hb[ix(i)] & 0xFF);
        }
    }

    private void _putInt(int i, int v) {
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[ix(i)] = (byte) (v >> 24);
            hb[ix(i + 1)] = (byte) (v >> 16);
            hb[ix(i + 2)] = (byte) (v >> 8);
            hb[ix(i + 3)] = (byte) v;
        } else {
            hb[ix(i)] = (byte) v;
            hb[ix(i + 1)] = (byte) (v >> 8);
            hb[ix(i + 2)] = (byte) (v >> 16);
            hb[ix(i + 3)] = (byte) (v >> 24);
        }
    }

    private long _getLong(int i) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return ((long) hb[ix(i)] << 56) |
                   ((long) (hb[ix(i + 1)] & 0xFF) << 48) |
                   ((long) (hb[ix(i + 2)] & 0xFF) << 40) |
                   ((long) (hb[ix(i + 3)] & 0xFF) << 32) |
                   ((long) (hb[ix(i + 4)] & 0xFF) << 24) |
                   ((long) (hb[ix(i + 5)] & 0xFF) << 16) |
                   ((long) (hb[ix(i + 6)] & 0xFF) << 8) |
                   ((long) (hb[ix(i + 7)] & 0xFF));
        } else {
            return ((long) hb[ix(i + 7)] << 56) |
                   ((long) (hb[ix(i + 6)] & 0xFF) << 48) |
                   ((long) (hb[ix(i + 5)] & 0xFF) << 40) |
                   ((long) (hb[ix(i + 4)] & 0xFF) << 32) |
                   ((long) (hb[ix(i + 3)] & 0xFF) << 24) |
                   ((long) (hb[ix(i + 2)] & 0xFF) << 16) |
                   ((long) (hb[ix(i + 1)] & 0xFF) << 8) |
                   ((long) (hb[ix(i)] & 0xFF));
        }
    }

    private void _putLong(int i, long v) {
        if (order == ByteOrder.BIG_ENDIAN) {
            hb[ix(i)] = (byte) (v >> 56);
            hb[ix(i + 1)] = (byte) (v >> 48);
            hb[ix(i + 2)] = (byte) (v >> 40);
            hb[ix(i + 3)] = (byte) (v >> 32);
            hb[ix(i + 4)] = (byte) (v >> 24);
            hb[ix(i + 5)] = (byte) (v >> 16);
            hb[ix(i + 6)] = (byte) (v >> 8);
            hb[ix(i + 7)] = (byte) v;
        } else {
            hb[ix(i)] = (byte) v;
            hb[ix(i + 1)] = (byte) (v >> 8);
            hb[ix(i + 2)] = (byte) (v >> 16);
            hb[ix(i + 3)] = (byte) (v >> 24);
            hb[ix(i + 4)] = (byte) (v >> 32);
            hb[ix(i + 5)] = (byte) (v >> 40);
            hb[ix(i + 6)] = (byte) (v >> 48);
            hb[ix(i + 7)] = (byte) (v >> 56);
        }
    }

    // -- Buffer operations --

    @Override
    public ByteBuffer slice() {
        int pos = position;
        int lim = limit;
        int rem = (pos <= lim ? lim - pos : 0);
        return new ByteBuffer(-1, 0, rem, rem, this.hb, ix(pos));
    }

    @Override
    public ByteBuffer slice(int index, int length) {
        return new ByteBuffer(-1, 0, length, length, this.hb, ix(index));
    }

    @Override
    public ByteBuffer duplicate() {
        ByteBuffer dup = new ByteBuffer(mark, position, limit, capacity, hb, offset);
        dup.isReadOnly = this.isReadOnly;
        dup.order = this.order;
        return dup;
    }

    public ByteBuffer asReadOnlyBuffer() {
        ByteBuffer buf = duplicate();
        buf.isReadOnly = true;
        return buf;
    }

    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public boolean hasArray() {
        return !isReadOnly && hb != null;
    }

    @Override
    public byte[] array() {
        if (hb == null)
            throw new UnsupportedOperationException();
        if (isReadOnly)
            throw new ReadOnlyBufferException();
        return hb;
    }

    @Override
    public int arrayOffset() {
        if (hb == null)
            throw new UnsupportedOperationException();
        if (isReadOnly)
            throw new ReadOnlyBufferException();
        return offset;
    }

    @Override
    public boolean isDirect() {
        return false; // No native memory in browser
    }

    // -- Byte order --

    public final ByteOrder order() {
        return order;
    }

    public final ByteBuffer order(ByteOrder bo) {
        this.order = bo;
        return this;
    }

    // -- Compact --

    public ByteBuffer compact() {
        if (isReadOnly)
            throw new ReadOnlyBufferException();
        int pos = position;
        int rem = remaining();
        System.arraycopy(hb, ix(pos), hb, ix(0), rem);
        position(rem);
        limit(capacity);
        discardMark();
        return this;
    }

    // -- View buffers --

    public CharBuffer asCharBuffer() {
        int size = remaining() >> 1;
        return new CharBuffer(hb, ix(position), size, order);
    }

    public ShortBuffer asShortBuffer() {
        int size = remaining() >> 1;
        return new ShortBuffer(hb, ix(position), size, order);
    }

    public IntBuffer asIntBuffer() {
        int size = remaining() >> 2;
        return new IntBuffer(hb, ix(position), size, order);
    }

    public LongBuffer asLongBuffer() {
        int size = remaining() >> 3;
        return new LongBuffer(hb, ix(position), size, order);
    }

    public FloatBuffer asFloatBuffer() {
        int size = remaining() >> 2;
        return new FloatBuffer(hb, ix(position), size, order);
    }

    public DoubleBuffer asDoubleBuffer() {
        int size = remaining() >> 3;
        return new DoubleBuffer(hb, ix(position), size, order);
    }

    // -- Comparison --

    @Override
    public int compareTo(ByteBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Byte.compare(this.get(i), that.get(j));
            if (cmp != 0)
                return cmp;
        }
        return this.remaining() - that.remaining();
    }

    @Override
    public boolean equals(Object ob) {
        if (!(ob instanceof ByteBuffer))
            return false;
        ByteBuffer that = (ByteBuffer) ob;
        if (this.remaining() != that.remaining())
            return false;
        int p = this.position();
        for (int i = this.limit() - 1, j = that.limit() - 1; i >= p; i--, j--) {
            if (this.get(i) != that.get(j))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 1;
        int p = position();
        for (int i = limit() - 1; i >= p; i--) {
            h = 31 * h + hb[ix(i)];
        }
        return h;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[pos=" + position() + " lim=" + limit() + " cap=" + capacity() + "]";
    }



    // -- Internal helpers --

    private void discardMark() {
        mark = -1;
    }

    static void checkBounds(int off, int len, int arrayLength) {
        if ((off | len | (off + len) | (arrayLength - (off + len))) < 0)
            throw new IndexOutOfBoundsException();
    }
}
