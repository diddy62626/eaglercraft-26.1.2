package java.io;

/**
 * TeaVM stub for java.io.FileInputStream.
 * Browser cannot access local files; all methods throw IOException.
 */
public class FileInputStream extends InputStream {

    private boolean closed = false;

    public FileInputStream(String name) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + name);
    }

    public FileInputStream(File file) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + file);
    }

    public FileInputStream(FileDescriptor fdObj) {
        // FileDescriptor is a stub; this is also effectively unusable
    }

    @Override
    public int read() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    @Override
    public long skip(long n) throws IOException {
        throw new IOException("Cannot skip in file in browser");
    }

    @Override
    public int available() throws IOException {
        return 0;
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
