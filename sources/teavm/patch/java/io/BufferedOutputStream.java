package java.io;
public class BufferedOutputStream extends FilterOutputStream {
    public BufferedOutputStream(OutputStream out) { super(out); }
    public BufferedOutputStream(OutputStream out, int size) { super(out); }
}