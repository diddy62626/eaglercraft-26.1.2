package java.io;

/**
 * TeaVM stub for java.io.FileOutputStream.
 * Browser cannot write local files; all methods throw IOException or no-op.
 */
public class FileOutputStream extends OutputStream {

    private boolean closed = false;

    public FileOutputStream(String name) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + name);
    }

    public FileOutputStream(String name, boolean append) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + name);
    }

    public FileOutputStream(File file) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + file);
    }

    public FileOutputStream(File file, boolean append) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + file);
    }

    public FileOutputStream(FileDescriptor fdObj) {
        // FileDescriptor is a stub; this is also effectively unusable
    }

    @Override
    public void write(int b) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    @Override
    public void write(byte[] b) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    @Override
    public void flush() throws IOException {
        // No-op
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public final FileDescriptor getFD() throws IOException {
        throw new IOException("No file descriptor in browser");
    }

    public java.nio.channels.FileChannel getChannel() {
        return null;
    }

    @Override
    protected void finalize() throws IOException {
        if (!closed) close();
    }
}
