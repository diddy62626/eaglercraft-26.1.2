package java.nio;

/**
 * TeaVM stub for java.nio.MappedByteBuffer.
 * No memory mapping in browser; extends ByteBuffer with stubs.
 */
public class MappedByteBuffer extends ByteBuffer {

    MappedByteBuffer(int mark, int pos, int lim, int cap, byte[] hb, int offset) {
        super(mark, pos, lim, cap, hb, offset);
    }

    MappedByteBuffer(int mark, int pos, int lim, int cap) {
        super(mark, pos, lim, cap);
    }

    public final boolean isLoaded() {
        return true;
    }

    public final MappedByteBuffer load() {
        return this;
    }

    public final MappedByteBuffer force() {
        return this;
    }

    public final MappedByteBuffer force(int index, int length) {
        return this;
    }

    @Override
    public MappedByteBuffer slice() {
        int pos = position;
        int rem = remaining();
        return new MappedByteBuffer(-1, 0, rem, rem, this.hb, ix(pos));
    }

    @Override
    public MappedByteBuffer slice(int index, int length) {
        return new MappedByteBuffer(-1, 0, length, length, this.hb, ix(index));
    }

    @Override
    public MappedByteBuffer duplicate() {
        MappedByteBuffer dup = new MappedByteBuffer(mark, position, limit, capacity, hb, offset);
        dup.isReadOnly = this.isReadOnly;
        dup.order = this.order;
        return dup;
    }

    @Override
    public ByteBuffer compact() {
        return super.compact();
    }

    private int ix(int i) {
        return i + offset;
    }
}
