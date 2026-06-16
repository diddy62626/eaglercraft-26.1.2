package java.io;

/**
 * TeaVM stub for java.io.ObjectInput.
 */
public interface ObjectInput extends DataInput {
    Object readObject() throws ClassNotFoundException, IOException;
    int read() throws IOException;
    int read(byte[] b) throws IOException;
    int read(byte[] b, int off, int len) throws IOException;
    long skip(long n) throws IOException;
    int available() throws IOException;
    void close() throws IOException;
}
