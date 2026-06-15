package java.nio;

import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class ShortBuffer extends Buffer implements Comparable<ShortBuffer> {
    short[] backingArray;
    ByteBuffer byteBuffer; // non-null if this is a view of a ByteBuffer
    boolean readOnly;
    int offset;

    // Constructor for allocate/wrap
    ShortBuffer(int capacity, short[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
        this.byteBuffer = null;
    }

    // Constructor for ByteBuffer view
    ShortBuffer(ByteBuffer bb) {
        super(bb.remaining() / 2, bb.remaining() / 2, 0);
        this.byteBuffer = bb;
        this.backingArray = null;
        this.offset = 0;
        this.readOnly = bb.isReadOnly();
    }

    // ========== Factory methods ==========

    public static ShortBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new ShortBuffer(capacity, new short[capacity], 0, false);
    }

    public static ShortBuffer wrap(short[] array) {
        return new ShortBuffer(array.length, array, 0, false);
    }

    public static ShortBuffer wrap(short[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        ShortBuffer buf = new ShortBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    // ========== Relative get/put ==========

    public short get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            short v = byteBuffer.getShort(byteBuffer.position + position * 2);
            position++;
            return v;
        }
        return backingArray[offset + position++];
    }

    public ShortBuffer put(short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            byteBuffer.putShort(byteBuffer.position + position * 2, value);
            position++;
            return this;
        }
        backingArray[offset + position++] = value;
        return this;
    }

    // ========== Absolute get/put ==========

    public short get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            return byteBuffer.getShort(byteBuffer.position + index * 2);
        }
        return backingArray[offset + index];
    }

    public ShortBuffer put(int index, short value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            byteBuffer.putShort(byteBuffer.position + index * 2, value);
            return this;
        }
        backingArray[offset + index] = value;
        return this;
    }

    // ========== Bulk get/put ==========

    public ShortBuffer get(short[] dst) {
        return get(dst, 0, dst.length);
    }

    public ShortBuffer get(short[] dst, int off, int len) {
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

    public ShortBuffer put(short[] src) {
        return put(src, 0, src.length);
    }

    public ShortBuffer put(short[] src, int off, int len) {
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

    public ShortBuffer put(ShortBuffer src) {
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

    public ShortBuffer slice() {
        if (byteBuffer != null) {
            ByteBuffer sliced = byteBuffer.slice();
            return new ShortBuffer(sliced);
        }
        int rem = remaining();
        return new ShortBuffer(rem, backingArray, offset + position, readOnly);
    }

    public ShortBuffer duplicate() {
        if (byteBuffer != null) {
            ShortBuffer buf = new ShortBuffer(byteBuffer.duplicate());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        ShortBuffer buf = new ShortBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public ShortBuffer asReadOnlyBuffer() {
        if (byteBuffer != null) {
            ShortBuffer buf = new ShortBuffer(byteBuffer.asReadOnlyBuffer());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        ShortBuffer buf = new ShortBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Array access ==========

    public final short[] array() {
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

    public ShortBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < remaining(); i++) {
                byteBuffer.putShort(byteBuffer.position + i * 2, byteBuffer.getShort(byteBuffer.position + position * 2 + i * 2));
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

    public int compareTo(ShortBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Short.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof ShortBuffer)) {
            return false;
        }
        ShortBuffer that = (ShortBuffer) ob;
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

    public Int16Array getInt16Array() {
        if (byteBuffer != null) {
            ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 2);
            Int16Array view = Int16Array.create(arrBuf);
            for (int i = 0; i < remaining(); i++) {
                view.set(i, get(position + i));
            }
            return view;
        }
        ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 2);
        Int16Array view = Int16Array.create(arrBuf);
        for (int i = 0; i < remaining(); i++) {
            view.set(i, backingArray[offset + position + i]);
        }
        return view;
    }

    public int getElementSize() {
        return 2;
    }
}
