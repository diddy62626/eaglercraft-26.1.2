package java.nio;

import org.teavm.jso.typedarrays.Uint8Array;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {
    byte[] backingArray;
    boolean readOnly;
    int offset; // offset into backingArray for sliced buffers

    ByteBuffer(int capacity, byte[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
    }

    // ========== Factory methods ==========

    public static ByteBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new ByteBuffer(capacity, new byte[capacity], 0, false);
    }

    public static ByteBuffer allocateDirect(int capacity) {
        return allocate(capacity);
    }

    public static ByteBuffer wrap(byte[] array) {
        return new ByteBuffer(array.length, array, 0, false);
    }

    public static ByteBuffer wrap(byte[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        ByteBuffer buf = new ByteBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    // ========== Relative get/put ==========

    public byte get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        return backingArray[offset + position++];
    }

    public ByteBuffer put(byte b) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        backingArray[offset + position++] = b;
        return this;
    }

    // ========== Absolute get/put ==========

    public byte get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException("index=" + index + ", limit=" + limit);
        }
        return backingArray[offset + index];
    }

    public ByteBuffer put(int index, byte b) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException("index=" + index + ", limit=" + limit);
        }
        backingArray[offset + index] = b;
        return this;
    }

    // ========== Bulk get/put ==========

    public ByteBuffer get(byte[] dst) {
        return get(dst, 0, dst.length);
    }

    public ByteBuffer get(byte[] dst, int off, int len) {
        if (off < 0 || len < 0 || off + len > dst.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len > remaining()) {
            throw new BufferUnderflowException();
        }
        System.arraycopy(backingArray, offset + position, dst, off, len);
        position += len;
        return this;
    }

    public ByteBuffer put(byte[] src) {
        return put(src, 0, src.length);
    }

    public ByteBuffer put(byte[] src, int off, int len) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (off < 0 || len < 0 || off + len > src.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len > remaining()) {
            throw new BufferOverflowException();
        }
        System.arraycopy(src, off, backingArray, offset + position, len);
        position += len;
        return this;
    }

    public ByteBuffer put(ByteBuffer src) {
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
        if (src.hasArray()) {
            System.arraycopy(src.backingArray, src.offset + src.position, backingArray, offset + position, srcRemaining);
        } else {
            for (int i = 0; i < srcRemaining; i++) {
                backingArray[offset + position + i] = src.get();
            }
        }
        src.position += srcRemaining;
        position += srcRemaining;
        return this;
    }

    // ========== Typed relative get/put (char - 2 bytes) ==========

    public char getChar() {
        int newPos = position + 2;
        if (newPos > limit) {
            throw new BufferUnderflowException();
        }
        char v = getCharUnchecked(position);
        position = newPos;
        return v;
    }

    public ByteBuffer putChar(char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        int newPos = position + 2;
        if (newPos > limit) {
            throw new BufferOverflowException();
        }
        putCharUnchecked(position, value);
        position = newPos;
        return this;
    }

    public char getChar(int index) {
        if (index < 0 || index + 2 > limit) {
            throw new IndexOutOfBoundsException();
        }
        return getCharUnchecked(index);
    }

    public ByteBuffer putChar(int index, char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 2 > limit) {
            throw new IndexOutOfBoundsException();
        }
        putCharUnchecked(index, value);
        return this;
    }

    private char getCharUnchecked(int index) {
        int base = offset + index;
        return (char) ((backingArray[base] & 0xFF) | ((backingArray[base + 1] & 0xFF) << 8));
    }

    private void putCharUnchecked(int index, char value) {
        int base = offset + index;
        backingArray[base] = (byte) (value & 0xFF);
        backingArray[base + 1] = (byte) ((value >> 8) & 0xFF);
    }

    // ========== Typed relative get/put (short - 2 bytes) ==========

    public short getShort() {
        int newPos = position + 2;
        if (newPos > limit) {
            throw new BufferUnderflowException();
        }
        short v = getShortUnchecked(position);
        position = newPos;
        return v;
    }

    public ByteBuffer putShort(short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        int newPos = position + 2;
        if (newPos > limit) {
            throw new BufferOverflowException();
        }
        putShortUnchecked(position, value);
        position = newPos;
        return this;
    }

    public short getShort(int index) {
        if (index < 0 || index + 2 > limit) {
            throw new IndexOutOfBoundsException();
        }
        return getShortUnchecked(index);
    }

    public ByteBuffer putShort(int index, short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 2 > limit) {
            throw new IndexOutOfBoundsException();
        }
        putShortUnchecked(index, value);
        return this;
    }

    private short getShortUnchecked(int index) {
        int base = offset + index;
        return (short) ((backingArray[base] & 0xFF) | ((backingArray[base + 1] & 0xFF) << 8));
    }

    private void putShortUnchecked(int index, short value) {
        int base = offset + index;
        backingArray[base] = (byte) (value & 0xFF);
        backingArray[base + 1] = (byte) ((value >> 8) & 0xFF);
    }

    // ========== Typed relative get/put (int - 4 bytes) ==========

    public int getInt() {
        int newPos = position + 4;
        if (newPos > limit) {
            throw new BufferUnderflowException();
        }
        int v = getIntUnchecked(position);
        position = newPos;
        return v;
    }

    public ByteBuffer putInt(int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        int newPos = position + 4;
        if (newPos > limit) {
            throw new BufferOverflowException();
        }
        putIntUnchecked(position, value);
        position = newPos;
        return this;
    }

    public int getInt(int index) {
        if (index < 0 || index + 4 > limit) {
            throw new IndexOutOfBoundsException();
        }
        return getIntUnchecked(index);
    }

    public ByteBuffer putInt(int index, int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 4 > limit) {
            throw new IndexOutOfBoundsException();
        }
        putIntUnchecked(index, value);
        return this;
    }

    private int getIntUnchecked(int index) {
        int base = offset + index;
        return ((backingArray[base] & 0xFF))
             | ((backingArray[base + 1] & 0xFF) << 8)
             | ((backingArray[base + 2] & 0xFF) << 16)
             | ((backingArray[base + 3] & 0xFF) << 24);
    }

    private void putIntUnchecked(int index, int value) {
        int base = offset + index;
        backingArray[base]     = (byte) (value & 0xFF);
        backingArray[base + 1] = (byte) ((value >> 8) & 0xFF);
        backingArray[base + 2] = (byte) ((value >> 16) & 0xFF);
        backingArray[base + 3] = (byte) ((value >> 24) & 0xFF);
    }

    // ========== Typed relative get/put (long - 8 bytes) ==========

    public long getLong() {
        int newPos = position + 8;
        if (newPos > limit) {
            throw new BufferUnderflowException();
        }
        long v = getLongUnchecked(position);
        position = newPos;
        return v;
    }

    public ByteBuffer putLong(long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        int newPos = position + 8;
        if (newPos > limit) {
            throw new BufferOverflowException();
        }
        putLongUnchecked(position, value);
        position = newPos;
        return this;
    }

    public long getLong(int index) {
        if (index < 0 || index + 8 > limit) {
            throw new IndexOutOfBoundsException();
        }
        return getLongUnchecked(index);
    }

    public ByteBuffer putLong(int index, long value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index + 8 > limit) {
            throw new IndexOutOfBoundsException();
        }
        putLongUnchecked(index, value);
        return this;
    }

    private long getLongUnchecked(int index) {
        int base = offset + index;
        return ((long) (backingArray[base] & 0xFF))
             | ((long) (backingArray[base + 1] & 0xFF) << 8)
             | ((long) (backingArray[base + 2] & 0xFF) << 16)
             | ((long) (backingArray[base + 3] & 0xFF) << 24)
             | ((long) (backingArray[base + 4] & 0xFF) << 32)
             | ((long) (backingArray[base + 5] & 0xFF) << 40)
             | ((long) (backingArray[base + 6] & 0xFF) << 48)
             | ((long) (backingArray[base + 7] & 0xFF) << 56);
    }

    private void putLongUnchecked(int index, long value) {
        int base = offset + index;
        backingArray[base]     = (byte) (value & 0xFF);
        backingArray[base + 1] = (byte) ((value >> 8) & 0xFF);
        backingArray[base + 2] = (byte) ((value >> 16) & 0xFF);
        backingArray[base + 3] = (byte) ((value >> 24) & 0xFF);
        backingArray[base + 4] = (byte) ((value >> 32) & 0xFF);
        backingArray[base + 5] = (byte) ((value >> 40) & 0xFF);
        backingArray[base + 6] = (byte) ((value >> 48) & 0xFF);
        backingArray[base + 7] = (byte) ((value >> 56) & 0xFF);
    }

    // ========== Typed relative get/put (float - 4 bytes) ==========

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public ByteBuffer putFloat(float value) {
        return putInt(Float.floatToRawIntBits(value));
    }

    public float getFloat(int index) {
        return Float.intBitsToFloat(getInt(index));
    }

    public ByteBuffer putFloat(int index, float value) {
        return putInt(index, Float.floatToRawIntBits(value));
    }

    // ========== Typed relative get/put (double - 8 bytes) ==========

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public ByteBuffer putDouble(double value) {
        return putLong(Double.doubleToRawLongBits(value));
    }

    public double getDouble(int index) {
        return Double.longBitsToDouble(getLong(index));
    }

    public ByteBuffer putDouble(int index, double value) {
        return putLong(index, Double.doubleToRawLongBits(value));
    }

    // ========== Typed buffer views ==========

    public CharBuffer asCharBuffer() {
        return new CharBuffer(this);
    }

    public ShortBuffer asShortBuffer() {
        return new ShortBuffer(this);
    }

    public IntBuffer asIntBuffer() {
        return new IntBuffer(this);
    }

    public LongBuffer asLongBuffer() {
        return new LongBuffer(this);
    }

    public FloatBuffer asFloatBuffer() {
        return new FloatBuffer(this);
    }

    public DoubleBuffer asDoubleBuffer() {
        return new DoubleBuffer(this);
    }

    // ========== Slice / duplicate / asReadOnly ==========

    public ByteBuffer slice() {
        int rem = remaining();
        return new ByteBuffer(rem, backingArray, offset + position, readOnly);
    }

    public ByteBuffer duplicate() {
        ByteBuffer buf = new ByteBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public ByteBuffer asReadOnlyBuffer() {
        ByteBuffer buf = new ByteBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Order ==========

    public ByteOrder order() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    public ByteBuffer order(ByteOrder order) {
        return this;
    }

    // ========== Array access ==========

    public final byte[] array() {
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

    public ByteBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        int rem = remaining();
        System.arraycopy(backingArray, offset + position, backingArray, offset, rem);
        position = rem;
        limit = capacity;
        mark = -1;
        return this;
    }

    // ========== Compare ==========

    public int compareTo(ByteBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Byte.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof ByteBuffer)) {
            return false;
        }
        ByteBuffer that = (ByteBuffer) ob;
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

    // ========== WebGL interop ==========

    public Uint8Array getUint8Array() {
        ArrayBuffer arrBuf = getArrayBuffer();
        return Uint8Array.create(arrBuf);
    }

    public ArrayBuffer getArrayBuffer() {
        ArrayBuffer arrBuf = ArrayBuffer.create(remaining());
        Uint8Array view = Uint8Array.create(arrBuf);
        for (int i = 0; i < remaining(); i++) {
            view.set(i, backingArray[offset + position + i]);
        }
        return arrBuf;
    }

    // ========== Internal constructor for typed buffer views ==========

    ByteBuffer(ByteBuffer underlying) {
        // Used by typed views - shares the same backing array
        super(0, 0, 0);
        this.backingArray = underlying.backingArray;
        this.offset = underlying.offset;
        this.readOnly = underlying.readOnly;
    }
}
