package java.util.zip;
public class InflaterInputStream extends java.io.FilterInputStream {
    public InflaterInputStream(java.io.InputStream in) { super(in); }
    public InflaterInputStream(java.io.InputStream in, Inflater inf) { super(in); }
    public InflaterInputStream(java.io.InputStream in, Inflater inf, int size) { super(in); }
    public int read() throws java.io.IOException { return -1; }
    public int read(byte[] b, int off, int len) throws java.io.IOException { return -1; }
    public void close() throws java.io.IOException {}
}