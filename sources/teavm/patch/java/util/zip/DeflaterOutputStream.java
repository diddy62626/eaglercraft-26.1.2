package java.util.zip;
public class DeflaterOutputStream extends java.io.FilterOutputStream {
    public DeflaterOutputStream(java.io.OutputStream out) { super(out); }
    public DeflaterOutputStream(java.io.OutputStream out, Deflater def) { super(out); }
    public void write(byte[] b, int off, int len) throws java.io.IOException {}
    public void close() throws java.io.IOException {}
}