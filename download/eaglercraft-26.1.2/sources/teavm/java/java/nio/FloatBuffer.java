package java.nio;

import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.ArrayBuffer;

public class FloatBuffer extends Buffer implements Comparable<FloatBuffer> {
    float[] backingArray;
    ByteBuffer byteBuffer; // non-null if this is a view of a ByteBuffer
    boolean readOnly;
    int offset;

    // Constructor for allocate/wrap
    FloatBuffer(int capacity, float[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
        this.byteBuffer = null;
    }

    // Constructor for ByteBuffer view
    FloatBuffer(ByteBuffer bb) {
        super(bb.remaining() / 4, bb.remaining() / 4, 0);
        this.byteBuffer = bb;
        this.backingArray = null;
        this.offset = 0;
        this.readOnly = bb.isReadOnly();
    }

    // ========== Factory methods ==========

    public static FloatBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new FloatBuffer(capacity, new float[capacity], 0, false);
    }

    public static FloatBuffer wrap(float[] array) {
        return new FloatBuffer(array.length, array, 0, false);
    }

    public static FloatBuffer wrap(float[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        FloatBuffer buf = new FloatBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    // ========== Relative get/put ==========

    public float get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            float v = byteBuffer.getFloat(byteBuffer.position + position * 4);
            position++;
            return v;
        }
        return backingArray[offset + position++];
    }

    public FloatBuffer put(float value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            byteBuffer.putFloat(byteBuffer.position + position * 4, value);
            position++;
            return this;
        }
        backingArray[offset + position++] = value;
        return this;
    }

    // ========== Absolute get/put ==========

    public float get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            return byteBuffer.getFloat(byteBuffer.position + index * 4);
        }
        return backingArray[offset + index];
    }

    public FloatBuffer put(int index, float value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            byteBuffer.putFloat(byteBuffer.position + index * 4, value);
            return this;
        }
        backingArray[offset + index] = value;
        return this;
    }

    // ========== Bulk get/put ==========

    public FloatBuffer get(float[] dst) {
        return get(dst, 0, dst.length);
    }

    public FloatBuffer get(float[] dst, int off, int len) {
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

    public FloatBuffer put(float[] src) {
        return put(src, 0, src.length);
    }

    public FloatBuffer put(float[] src, int off, int len) {
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

    public FloatBuffer put(FloatBuffer src) {
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

    public FloatBuffer slice() {
        if (byteBuffer != null) {
            ByteBuffer sliced = byteBuffer.slice();
            return new FloatBuffer(sliced);
        }
        int rem = remaining();
        return new FloatBuffer(rem, backingArray, offset + position, readOnly);
    }

    public FloatBuffer duplicate() {
        if (byteBuffer != null) {
            FloatBuffer buf = new FloatBuffer(byteBuffer.duplicate());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        FloatBuffer buf = new FloatBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public FloatBuffer asReadOnlyBuffer() {
        if (byteBuffer != null) {
            FloatBuffer buf = new FloatBuffer(byteBuffer.asReadOnlyBuffer());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        FloatBuffer buf = new FloatBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Array access ==========

    public final float[] array() {
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

    public FloatBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < remaining(); i++) {
                byteBuffer.putFloat(byteBuffer.position + i * 4, byteBuffer.getFloat(byteBuffer.position + position * 4 + i * 4));
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

    public int compareTo(FloatBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Float.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof FloatBuffer)) {
            return false;
        }
        FloatBuffer that = (FloatBuffer) ob;
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

    public Float32Array getFloat32Array() {
        if (byteBuffer != null) {
            ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 4);
            Float32Array view = Float32Array.create(arrBuf);
            for (int i = 0; i < remaining(); i++) {
                view.set(i, get(position + i));
            }
            return view;
        }
        ArrayBuffer arrBuf = ArrayBuffer.create(remaining() * 4);
        Float32Array view = Float32Array.create(arrBuf);
        for (int i = 0; i < remaining(); i++) {
            view.set(i, backingArray[offset + position + i]);
        }
        return view;
    }

    public int getElementSize() {
        return 4;
    }
}
