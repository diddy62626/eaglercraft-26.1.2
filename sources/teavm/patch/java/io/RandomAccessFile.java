package java.io;

import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * TeaVM stub for java.io.RandomAccessFile.
 * Browser cannot access local files; all methods throw IOException.
 */
public class RandomAccessFile implements DataOutput, DataInput, Closeable {

    private boolean closed = false;

    public RandomAccessFile(String name, String mode) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + name);
    }

    public RandomAccessFile(File file, String mode) throws FileNotFoundException {
        throw new FileNotFoundException("Cannot open file in browser: " + file);
    }

    public final FileDescriptor getFD() throws IOException {
        throw new IOException("No file descriptor in browser");
    }

    public final FileChannel getChannel() {
        return null;
    }

    // -- Read operations (all throw) --

    public int read() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public int read(byte[] b) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public int read(byte[] b, int off, int len) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final void readFully(byte[] b) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public int skipBytes(int n) throws IOException {
        throw new IOException("Cannot skip in file in browser");
    }

    // -- Write operations (all throw) --

    public void write(int b) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public void write(byte[] b) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public void write(byte[] b, int off, int len) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeBoolean(boolean v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeByte(int v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeShort(int v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeChar(int v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeInt(int v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeLong(long v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeFloat(float v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeDouble(double v) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeBytes(String s) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeChars(String s) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    public final void writeUTF(String str) throws IOException {
        throw new IOException("Cannot write to file in browser");
    }

    // -- DataInput read operations (all throw) --

    public final boolean readBoolean() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final byte readByte() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final int readUnsignedByte() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final short readShort() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final int readUnsignedShort() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final char readChar() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final int readInt() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final long readLong() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final float readFloat() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final double readDouble() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final String readLine() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    public final String readUTF() throws IOException {
        throw new IOException("Cannot read from file in browser");
    }

    // -- File pointer / seeking --

    public long getFilePointer() throws IOException {
        return 0;
    }

    public void seek(long pos) throws IOException {
        throw new IOException("Cannot seek in file in browser");
    }

    public long length() throws IOException {
        return 0;
    }

    public void setLength(long newLength) throws IOException {
        throw new IOException("Cannot set length in browser");
    }

    public void close() throws IOException {
        closed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (!closed) close();
        } finally {
            super.finalize();
        }
    }
}
