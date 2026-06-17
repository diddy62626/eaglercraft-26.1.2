package java.nio.channels;

import java.io.IOException;

/**
 * TeaVM stub for java.nio.channels.FileLock.
 */
public class FileLock implements AutoCloseable {

    private final FileChannel channel;
    private final long position;
    private final long size;
    private final boolean shared;
    private volatile boolean valid = true;

    protected FileLock(FileChannel channel, long position, long size, boolean shared) {
        this.channel = channel;
        this.position = position;
        this.size = size;
        this.shared = shared;
    }

    public final FileChannel channel() {
        return channel;
    }

    public final long position() {
        return position;
    }

    public final long size() {
        return size;
    }

    public final boolean isShared() {
        return shared;
    }

    public final boolean isValid() {
        return valid;
    }

    public void release() throws IOException {
        valid = false;
    }

    @Override
    public void close() throws IOException {
        release();
    }

    @Override
    public String toString() {
        return "FileLock[" + position + ":" + size + " " + (shared ? "shared" : "exclusive") + " " + (valid ? "valid" : "invalid") + "]";
    }
}
