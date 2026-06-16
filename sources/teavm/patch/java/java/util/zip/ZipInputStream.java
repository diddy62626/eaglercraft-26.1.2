package java.util.zip;
public class ZipInputStream extends InflaterInputStream {
    public ZipInputStream(java.io.InputStream in) { super(in); }
    public ZipEntry getNextEntry() throws java.io.IOException { return null; }
    public void closeEntry() throws java.io.IOException {}
}