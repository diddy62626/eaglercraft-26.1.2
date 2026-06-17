package java.util.zip;
public class ZipOutputStream extends DeflaterOutputStream {
    public ZipOutputStream(java.io.OutputStream out) { super(out); }
    public void putNextEntry(ZipEntry e) throws java.io.IOException {}
    public void closeEntry() throws java.io.IOException {}
}