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

    public static FileChannel open(java.nio.file.Path path, java.util.Set<? extends java.nio.file.OpenOption> options,
                                    java.nio.file.attribute.FileAttribute<?>... attrs) throws IOException {
        throw new IOException("Cannot open file channel in browser");
    }

    public static FileChannel open(java.nio.file.Path path, java.nio.file.OpenOption... options) throws IOException {
        throw new IOException("Cannot open file channel in browser");
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
