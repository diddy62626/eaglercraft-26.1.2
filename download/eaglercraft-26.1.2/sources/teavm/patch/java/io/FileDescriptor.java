package java.io;

/**
 * TeaVM stub for java.io.FileDescriptor.
 */
public final class FileDescriptor {

    public static final FileDescriptor in = new FileDescriptor();
    public static final FileDescriptor out = new FileDescriptor();
    public static final FileDescriptor err = new FileDescriptor();

    private boolean closed = false;

    public boolean valid() {
        return !closed;
    }

    public void sync() throws SyncFailedException {
        // No-op in browser
    }
}
