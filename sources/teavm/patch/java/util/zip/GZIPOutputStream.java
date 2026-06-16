package java.util.zip;
public class GZIPOutputStream extends DeflaterOutputStream {
    public GZIPOutputStream(java.io.OutputStream out) { super(out); }
    public GZIPOutputStream(java.io.OutputStream out, int size) { super(out); }
}