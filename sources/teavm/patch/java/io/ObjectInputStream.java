package java.io;

/**
 * TeaVM stub for java.io.ObjectInputStream.
 * Browser: object deserialization is not supported.
 */
public class ObjectInputStream extends InputStream implements ObjectInput, ObjectStreamConstants {
    public ObjectInputStream(InputStream in) throws IOException {}

    public final Object readObject() throws IOException, ClassNotFoundException {
        throw new IOException("Object deserialization not supported in browser");
    }

    public int read() throws IOException { return -1; }
    public void close() throws IOException {}
    public int available() throws IOException { return 0; }

    public void defaultReadObject() throws IOException, ClassNotFoundException {}
    public ObjectInputStream.GetField readFields() throws IOException {
        return new GetField();
    }

    // ===== DataInput methods =====
    public void readFully(byte[] b) throws IOException {}
    public void readFully(byte[] b, int off, int len) throws IOException {}
    public int skipBytes(int n) throws IOException { return 0; }
    public boolean readBoolean() throws IOException { return false; }
    public byte readByte() throws IOException { return 0; }
    public int readUnsignedByte() throws IOException { return 0; }
    public short readShort() throws IOException { return 0; }
    public int readUnsignedShort() throws IOException { return 0; }
    public char readChar() throws IOException { return 0; }
    public int readInt() throws IOException { return 0; }
    public long readLong() throws IOException { return 0L; }
    public float readFloat() throws IOException { return 0f; }
    public double readDouble() throws IOException { return 0.0; }
    public String readLine() throws IOException { return null; }
    public String readUTF() throws IOException { return ""; }

    public static class GetField {
        public Object get(String name, Object def) { return def; }
        public boolean get(String name, boolean def) { return def; }
        public byte get(String name, byte def) { return def; }
        public char get(String name, char def) { return def; }
        public short get(String name, short def) { return def; }
        public int get(String name, int def) { return def; }
        public long get(String name, long def) { return def; }
        public float get(String name, float def) { return def; }
        public double get(String name, double def) { return def; }
    }
}
