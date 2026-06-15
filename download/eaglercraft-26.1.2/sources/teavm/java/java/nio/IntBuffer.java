package java.nio;

import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class IntBuffer extends Buffer implements Comparable<IntBuffer> {
    int[] backingArray;
    ByteBuffer byteBuffer; // non-null if this is a view of a ByteBuffer
    boolean readOnly;
    int offset; // offset into backingArray

    // Constructor for allocate/wrap
    IntBuffer(int capacity, int[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
        this.byteBuffer = null;
    }

    // Constructor for ByteBuffer view
    IntBuffer(ByteBuffer bb) {
        super(bb.remaining() / 4, bb.remaining() / 4, 0);
        this.byteBuffer = bb;
        this.backingArray = null;
        this.offset = 0;
        this.readOnly = bb.isReadOnly();
    }

    // ========== Factory methods ==========

    public static IntBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new IntBuffer(capacity, new int[capacity], 0, false);
    }

    public static IntBuffer wrap(int[] array) {
        return new IntBuffer(array.length, array, 0, false);
    }

    public static IntBuffer wrap(int[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        IntBuffer buf = new IntBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    // ========== Relative get/put ==========

    public int get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            int v = byteBuffer.getInt(byteBuffer.position + position * 4);
            position++;
            return v;
        }
        return backingArray[offset + position++];
    }

    public IntBuffer put(int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            byteBuffer.putInt(byteBuffer.position + position * 4, value);
            position++;
            return this;
        }
        backingArray[offset + position++] = value;
        return this;
    }

    // ========== Absolute get/put ==========

    public int get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            return byteBuffer.getInt(byteBuffer.position + index * 4);
        }
        return backingArray[offset + index];
    }

    public IntBuffer put(int index, int value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            byteBuffer.putInt(byteBuffer.position + index * 4, value);
            return this;
        }
        backingArray[offset + index] = value;
        return this;
    }

    // ========== Bulk get/put ==========

    public IntBuffer get(int[] dst) {
        return get(dst, 0, dst.length);
    }

    public IntBuffer get(int[] dst, int off, int len) {
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

    public IntBuffer put(int[] src) {
        return put(src, 0, src.length);
    }

    public IntBuffer put(int[] src, int off, int len) {
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

    public IntBuffer put(IntBuffer src) {
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

    public IntBuffer slice() {
        if (byteBuffer != null) {
            ByteBuffer sliced = byteBuffer.slice();
            return new IntBuffer(sliced);
        }
        int rem = remaining();
        return new IntBuffer(rem, backingArray, offset + position, readOnly);
    }

    public IntBuffer duplicate() {
        if (byteBuffer != null) {
            IntBuffer buf = new IntBuffer(byteBuffer.duplicate());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        IntBuffer buf = new IntBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public IntBuffer asReadOnlyBuffer() {
        if (byteBuffer != null) {
            IntBuffer buf = new IntBuffer(byteBuffer.asReadOnlyBuffer());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        IntBuffer buf = new IntBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Array access ==========

    public final int[] array() {
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

    public IntBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < remaining(); i++) {
                byteBuffer.putInt(byteBuffer.position + i * 4, byteBuffer.getInt(byteBuffer.position + position * 4 + i * 4));
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

    public int compareTo(IntBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Integer.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof IntBuffer)) {
            return false;
        }
        IntBuffer that = (IntBuffer) ob;
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

    public Int32Array getInt32Array() {
        if (byteBuffer != null) {
            ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 4);
            Int32Array view = Int32Array.create(arrBuf);
            for (int i = 0; i < remaining(); i++) {
                view.set(i, get(position + i));
            }
            return view;
        }
        ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 4);
        Int32Array view = Int32Array.create(arrBuf);
        for (int i = 0; i < remaining(); i++) {
            view.set(i, backingArray[offset + position + i]);
        }
        return view;
    }

    public int getElementSize() {
        return 4;
    }
}
