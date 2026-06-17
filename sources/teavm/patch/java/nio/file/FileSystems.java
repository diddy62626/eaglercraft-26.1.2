package java.nio.file;
public class FileSystems {
    public static FileSystem getDefault() { return new DefaultFileSystem(); }
    public static FileSystem getFileSystem(java.net.URI uri) { return getDefault(); }
    public static FileSystem newFileSystem(java.net.URI uri, java.util.Map<String,?> env) { return getDefault(); }
}