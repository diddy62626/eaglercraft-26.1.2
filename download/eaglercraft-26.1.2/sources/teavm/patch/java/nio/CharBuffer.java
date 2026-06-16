package java.nio;

/**
 * TeaVM stub for java.nio.CharBuffer.
 * View over ByteBuffer's backing array with proper byte order.
 */
public class CharBuffer extends Buffer implements Comparable<CharBuffer>, CharSequence, Appendable, Readable {

    final byte[] hb;
    final int offset;
    final ByteOrder order;
    private boolean isReadOnly;

    // Standalone allocation
    CharBuffer(int mark, int pos, int lim, int cap, char[] array, int offset) {
        super(mark, pos, lim, cap);
        // Convert char[] to byte[] for unified storage
        this.hb = new byte[cap * 2];
        this.offset = 0;
        this.order = ByteOrder.BIG_ENDIAN;
        this.isReadOnly = false;
    }

    // View over ByteBuffer
    CharBuffer(byte[] hb, int offset, int size, ByteOrder order) {
        super(-1, 0, size, size);
        this.hb = hb;
        this.offset = offset;
        this.order = order;
        this.isReadOnly = false;
    }

    public static CharBuffer allocate(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException();
        return new CharBuffer(-1, 0, capacity, capacity, new char[capacity], 0);
    }

    public static CharBuffer wrap(char[] array) {
        return wrap(array, 0, array.length);
    }

    public static CharBuffer wrap(char[] array, int offset, int length) {
        CharBuffer buf = allocate(array.length);
        for (int i = 0; i < length; i++)
            buf.put(offset + i, array[offset + i]);
        buf.position(offset);
        buf.limit(offset + length);
        return buf;
    }

    public static CharBuffer wrap(CharSequence csq) {
        return wrap(csq, 0, csq.length());
    }

    public static CharBuffer wrap(CharSequence csq, int start, int end) {
        CharBuffer buf = allocate(csq.length());
        for (int i = start; i < end; i++)
            buf.put(csq.charAt(i));
        buf.flip();
        return buf;
    }

    public char get() {
        return getChar(nextGetIndex());
    }

    public char get(int i) {
        return getChar(checkIndex(i));
    }

    public CharBuffer get(char[] dst) {
        return get(dst, 0, dst.length);
    }

    public CharBuffer get(char[] dst, int offset, int length) {
        checkBounds(offset, length, dst.length);
        if (length > remaining())
            throw new BufferUnderflowException();
        for (int i = offset; i < offset + length; i++)
            dst[i] = get();
        return this;
    }

    public CharBuffer put(char c) {
        putChar(nextPutIndex(), c);
        return this;
    }

    public CharBuffer put(int i, char c) {
        putChar(checkIndex(i), c);
        return this;
    }

    public CharBuffer put(CharBuffer src) {
        if (src == this) throw new IllegalArgumentException();
        int n = src.remaining();
        if (n > remaining()) throw new BufferOverflowException();
        for (int i = 0; i < n; i++) put(src.get());
        return this;
    }

    public CharBuffer put(char[] src) {
        return put(src, 0, src.length);
    }

    public CharBuffer put(char[] src, int offset, int length) {
        checkBounds(offset, length, src.length);
        if (length > remaining()) throw new BufferOverflowException();
        for (int i = offset; i < offset + length; i++) put(src[i]);
        return this;
    }

    public CharBuffer put(String src) {
        return put(src, 0, src.length());
    }

    public CharBuffer put(String src, int start, int end) {
        for (int i = start; i < end; i++) put(src.charAt(i));
        return this;
    }

    @Override
    public CharBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new CharBuffer(hb, offset + pos * 2, rem, order);
    }

    @Override
    public CharBuffer slice(int index, int length) {
        return new CharBuffer(hb, offset + index * 2, length, order);
    }

    @Override
    public CharBuffer duplicate() {
        CharBuffer dup = new CharBuffer(hb, offset, capacity, order);
        dup.position = this.position;
        dup.limit = this.limit;
        dup.mark = this.mark;
        return dup;
    }

    public CharBuffer asReadOnlyBuffer() {
        CharBuffer buf = duplicate();
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

    public CharBuffer compact() {
        int pos = position;
        int rem = remaining();
        for (int i = 0; i < rem; i++)
            putChar(i, getChar(pos + i));
        position(rem);
        limit(capacity);
        mark = -1;
        return this;
    }

    // -- CharSequence --

    @Override
    public int length() { return remaining(); }

    @Override
    public char charAt(int index) { return get(position + index); }

    @Override
    public CharSequence subSequence(int start, int end) {
        int p = position;
        return new CharBuffer(hb, offset + (p + start) * 2, end - start, order);
    }

    @Override
    public String toString() {
        return toStringImpl();
    }

    private String toStringImpl() {
        int len = remaining();
        char[] chars = new char[len];
        for (int i = 0; i < len; i++)
            chars[i] = getChar(position + i);
        return new String(chars);
    }

    // -- Appendable --

    @Override
    public CharBuffer append(CharSequence csq) {
        return put(csq == null ? "null" : csq.toString());
    }

    @Override
    public CharBuffer append(CharSequence csq, int start, int end) {
        return put(csq == null ? "null" : csq.subSequence(start, end).toString());
    }

    @Override
    public CharBuffer append(char c) {
        return put(c);
    }

    // -- Readable --

    public int read(CharBuffer target) {
        int n = remaining();
        if (n == 0) return -1;
        for (int i = 0; i < n; i++) target.put(get());
        return n;
    }

    // -- Comparison --

    @Override
    public int compareTo(CharBuffer that) {
        int n = this.position() + Math.min(this.remaining(), that.remaining());
        for (int i = this.position(), j = that.position(); i < n; i++, j++) {
            int cmp = Character.compare(this.get(i), that.get(j));
            if (cmp != 0) return cmp;
        }
        return this.remaining() - that.remaining();
    }

    // -- Byte order-aware char read/write --

    private char getChar(int i) {
        int bytePos = offset + i * 2;
        if (order == ByteOrder.BIG_ENDIAN) {
            return (char) ((hb[bytePos] << 8) | (hb[bytePos + 1] & 0xFF));
        } else {
            return (char) ((hb[bytePos + 1] << 8) | (hb[bytePos] & 0xFF));
        }
    }

    private void putChar(int i, char v) {
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
