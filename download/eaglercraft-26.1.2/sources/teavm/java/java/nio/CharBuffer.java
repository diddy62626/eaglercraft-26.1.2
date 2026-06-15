package java.nio;

public class CharBuffer extends Buffer implements Comparable<CharBuffer>, Appendable, CharSequence, Readable {
    char[] backingArray;
    ByteBuffer byteBuffer; // non-null if this is a view of a ByteBuffer
    boolean readOnly;
    int offset;

    // Constructor for allocate/wrap
    CharBuffer(int capacity, char[] backingArray, int offset, boolean readOnly) {
        super(capacity, capacity, 0);
        this.backingArray = backingArray;
        this.offset = offset;
        this.readOnly = readOnly;
        this.byteBuffer = null;
    }

    // Constructor for ByteBuffer view
    CharBuffer(ByteBuffer bb) {
        super(bb.remaining() / 2, bb.remaining() / 2, 0);
        this.byteBuffer = bb;
        this.backingArray = null;
        this.offset = 0;
        this.readOnly = bb.isReadOnly();
    }

    // ========== Factory methods ==========

    public static CharBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity < 0: " + capacity);
        }
        return new CharBuffer(capacity, new char[capacity], 0, false);
    }

    public static CharBuffer wrap(char[] array) {
        return new CharBuffer(array.length, array, 0, false);
    }

    public static CharBuffer wrap(char[] array, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > array.length) {
            throw new IndexOutOfBoundsException();
        }
        CharBuffer buf = new CharBuffer(array.length, array, 0, false);
        buf.position = offset;
        buf.limit = offset + length;
        return buf;
    }

    public static CharBuffer wrap(CharSequence csq) {
        return wrap(csq, 0, csq.length());
    }

    public static CharBuffer wrap(CharSequence csq, int start, int end) {
        int len = end - start;
        CharBuffer buf = allocate(len);
        for (int i = start; i < end; i++) {
            buf.put(csq.charAt(i));
        }
        buf.flip();
        return buf;
    }

    // ========== Relative get/put ==========

    public char get() {
        if (position >= limit) {
            throw new BufferUnderflowException();
        }
        if (byteBuffer != null) {
            char v = byteBuffer.getChar(byteBuffer.position + position * 2);
            position++;
            return v;
        }
        return backingArray[offset + position++];
    }

    public CharBuffer put(char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (position >= limit) {
            throw new BufferOverflowException();
        }
        if (byteBuffer != null) {
            byteBuffer.putChar(byteBuffer.position + position * 2, value);
            position++;
            return this;
        }
        backingArray[offset + position++] = value;
        return this;
    }

    // ========== Absolute get/put ==========

    public char get(int index) {
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            return byteBuffer.getChar(byteBuffer.position + index * 2);
        }
        return backingArray[offset + index];
    }

    public CharBuffer put(int index, char value) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (index < 0 || index >= limit) {
            throw new IndexOutOfBoundsException();
        }
        if (byteBuffer != null) {
            byteBuffer.putChar(byteBuffer.position + index * 2, value);
            return this;
        }
        backingArray[offset + index] = value;
        return this;
    }

    // ========== Bulk get/put ==========

    public CharBuffer get(char[] dst) {
        return get(dst, 0, dst.length);
    }

    public CharBuffer get(char[] dst, int off, int len) {
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

    public CharBuffer put(char[] src) {
        return put(src, 0, src.length);
    }

    public CharBuffer put(char[] src, int off, int len) {
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

    public CharBuffer put(CharBuffer src) {
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

    public CharBuffer put(String src) {
        return put(src, 0, src.length());
    }

    public CharBuffer put(String src, int start, int end) {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (start < 0 || end < 0 || start > end || end - start > remaining()) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = start; i < end; i++) {
            put(src.charAt(i));
        }
        return this;
    }

    // ========== Slice / duplicate / asReadOnly ==========

    public CharBuffer slice() {
        if (byteBuffer != null) {
            ByteBuffer sliced = byteBuffer.slice();
            return new CharBuffer(sliced);
        }
        int rem = remaining();
        return new CharBuffer(rem, backingArray, offset + position, readOnly);
    }

    public CharBuffer duplicate() {
        if (byteBuffer != null) {
            CharBuffer buf = new CharBuffer(byteBuffer.duplicate());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        CharBuffer buf = new CharBuffer(capacity, backingArray, offset, readOnly);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    public CharBuffer asReadOnlyBuffer() {
        if (byteBuffer != null) {
            CharBuffer buf = new CharBuffer(byteBuffer.asReadOnlyBuffer());
            buf.position = position;
            buf.limit = limit;
            buf.mark = mark;
            return buf;
        }
        CharBuffer buf = new CharBuffer(capacity, backingArray, offset, true);
        buf.position = position;
        buf.limit = limit;
        buf.mark = mark;
        return buf;
    }

    // ========== Array access ==========

    public final char[] array() {
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

    public CharBuffer compact() {
        if (readOnly) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer != null) {
            for (int i = 0; i < remaining(); i++) {
                byteBuffer.putChar(byteBuffer.position + i * 2, byteBuffer.getChar(byteBuffer.position + position * 2 + i * 2));
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

    public int compareTo(CharBuffer that) {
        int n = this.position + Math.min(this.remaining(), that.remaining());
        for (int i = this.position, j = that.position; i < n; i++, j++) {
            int cmp = Character.compare(this.get(i), that.get(j));
            if (cmp != 0) {
                return cmp;
            }
        }
        return this.remaining() - that.remaining();
    }

    public boolean equals(Object ob) {
        if (!(ob instanceof CharBuffer)) {
            return false;
        }
        CharBuffer that = (CharBuffer) ob;
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

    // ========== CharSequence ==========

    public int length() {
        return remaining();
    }

    public char charAt(int index) {
        return get(position + index);
    }

    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end < 0 || start > end || end > remaining()) {
            throw new IndexOutOfBoundsException();
        }
        CharBuffer buf = duplicate();
        buf.position = position + start;
        buf.limit = position + end;
        return buf;
    }

    // ========== Appendable ==========

    public CharBuffer append(CharSequence csq) {
        if (csq == null) {
            return put("null");
        }
        return put(csq.toString());
    }

    public CharBuffer append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        return put(csq.subSequence(start, end).toString());
    }

    public CharBuffer append(char c) {
        return put(c);
    }

    // ========== Readable ==========

    public java.nio.CharBuffer read(java.nio.CharBuffer target) {
        int len = remaining();
        if (len == 0) {
            return -1 < 0 ? null : this;
        }
        if (len > target.remaining()) {
            len = target.remaining();
        }
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                target.put(get());
            }
        }
        return len > 0 || -1 < 0 ? this : null;
    }

    // ========== toString ==========

    public String toString() {
        if (backingArray != null) {
            return new String(backingArray, offset + position, remaining());
        }
        StringBuilder sb = new StringBuilder(remaining());
        for (int i = position; i < limit; i++) {
            sb.append(get(i));
        }
        return sb.toString();
    }
}
