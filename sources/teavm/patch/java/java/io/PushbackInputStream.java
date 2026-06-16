package java.io;
public class PushbackInputStream extends FilterInputStream {
    public PushbackInputStream(InputStream in) { super(in); }
    public PushbackInputStream(InputStream in, int size) { super(in); }
    public void unread(int b) throws IOException {}
    public void unread(byte[] b) throws IOException {}
    public void unread(byte[] b, int off, int len) throws IOException {}
}