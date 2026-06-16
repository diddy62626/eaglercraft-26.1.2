package java.util.zip;
public class GZIPInputStream extends InflaterInputStream {
    public GZIPInputStream(java.io.InputStream in) throws java.io.IOException { super(in); }
    public GZIPInputStream(java.io.InputStream in, int size) throws java.io.IOException { super(in); }
}