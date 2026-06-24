package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.spi.AbstractInterruptibleChannel;

/**
 * TeaVM stub for java.nio.channels.FileChannel.
 * Browser cannot access files; all methods throw IOException or return defaults.
 */
public abstract class FileChannel extends AbstractInterruptibleChannel
        implements SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel {

    protected FileChannel() {
    }

    // -- Read operations --

    public abstract int read(ByteBuffer dst) throws IOException;

    public abstract long read(ByteBuffer[] dsts, int offset, int length) throws IOException;

    public final long read(ByteBuffer[] dsts) throws IOException {
        return read(dsts, 0, dsts.length);
    }

    public abstract int read(ByteBuffer dst, long position) throws IOException;

    // -- Write operations --

    public abstract int write(ByteBuffer src) throws IOException;

    public abstract long write(ByteBuffer[] srcs, int offset, int length) throws IOException;

    public final long write(ByteBuffer[] srcs) throws IOException {
        return write(srcs, 0, srcs.length);
    }

    public abstract int write(ByteBuffer src, long position) throws IOException;

    // -- Position / size --

    public abstract long position() throws IOException;

    public abstract FileChannel position(long newPosition) throws IOException;

    public abstract long size() throws IOException;

    public abstract FileChannel truncate(long size) throws IOException;

    // -- Force --

    public abstract void force(boolean metaData) throws IOException;

    // -- Transfer --

    public abstract long transferTo(long position, long count, WritableByteChannel target) throws IOException;

    public abstract long transferFrom(ReadableByteChannel src, long position, long count) throws IOException;

    // -- Map --

    public abstract MappedByteBuffer map(MapMode mode, long position, long size) throws IOException;

    // -- Lock --

    public abstract FileLock lock(long position, long size, boolean shared) throws IOException;

    public final FileLock lock() throws IOException {
        return lock(0L, Long.MAX_VALUE, false);
    }

    public abstract FileLock tryLock(long position, long size, boolean shared) throws IOException;

    public final FileLock tryLock() throws IOException {
        return tryLock(0L, Long.MAX_VALUE, false);
    }

    // -- Open --
    // In a browser, we can't open real file channels. Instead of throwing
    // (which crashes MC's DataFixers initialization), return a fake
    // in-memory channel that absorbs all reads/writes. This lets the
    // game continue past the file I/O code path.

    public static FileChannel open(java.nio.file.Path path, java.util.Set<? extends java.nio.file.OpenOption> options,
                                    java.nio.file.attribute.FileAttribute<?>... attrs) throws IOException {
        return new FakeFileChannel();
    }

    public static FileChannel open(java.nio.file.Path path, java.nio.file.OpenOption... options) throws IOException {
        return new FakeFileChannel();
    }

    // Fake in-memory FileChannel that absorbs all operations
    private static class FakeFileChannel extends FileChannel {
        private long pos = 0;
        private long sizeVal = 0;

        @Override
        public int read(ByteBuffer dst) throws IOException { return -1; }
        @Override
        public long read(ByteBuffer[] dsts, int offset, int length) throws IOException { return -1; }
        @Override
        public int read(ByteBuffer dst, long position) throws IOException { return -1; }
        @Override
        public int write(ByteBuffer src) throws IOException {
            int n = src.remaining();
            pos += n;
            sizeVal = Math.max(sizeVal, pos);
            src.position(src.limit());
            return n;
        }
        @Override
        public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
            long total = 0;
            for (int i = offset; i < offset + length; i++) { total += write(srcs[i]); }
            return total;
        }
        @Override
        public int write(ByteBuffer src, long position) throws IOException { return write(src); }
        @Override
        public long position() throws IOException { return pos; }
        @Override
        public FileChannel position(long newPosition) throws IOException { pos = newPosition; return this; }
        @Override
        public long size() throws IOException { return sizeVal; }
        @Override
        public FileChannel truncate(long size) throws IOException {
            sizeVal = Math.min(sizeVal, size);
            if (pos > sizeVal) pos = sizeVal;
            return this;
        }
        @Override
        public void force(boolean metaData) throws IOException { }
        @Override
        public long transferTo(long position, long count, WritableByteChannel target) throws IOException { return 0; }
        @Override
        public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException { return 0; }
        @Override
        public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
            return null;
        }
        @Override
        public FileLock lock(long position, long size, boolean shared) throws IOException { return null; }
        @Override
        public FileLock tryLock(long position, long size, boolean shared) throws IOException { return null; }
        @Override
        protected void implCloseChannel() throws IOException { }
    }

    // -- MapMode inner class --

    public static class MapMode {
        public static final MapMode READ_ONLY = new MapMode("READ_ONLY");
        public static final MapMode READ_WRITE = new MapMode("READ_WRITE");
        public static final MapMode PRIVATE = new MapMode("PRIVATE");

        private final String name;

        private MapMode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
