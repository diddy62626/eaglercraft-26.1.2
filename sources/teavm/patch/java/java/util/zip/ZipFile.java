package java.util.zip;
public class ZipFile {
    public ZipFile(String name) throws java.io.IOException {}
    public ZipFile(java.io.File file) throws java.io.IOException {}
    public java.util.Enumeration<? extends ZipEntry> entries() { return java.util.Collections.enumeration(java.util.Collections.emptyList()); }
    public java.io.InputStream getInputStream(ZipEntry entry) throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
    public void close() throws java.io.IOException {}
}