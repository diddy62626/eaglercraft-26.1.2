package java.nio.file;
public class NotDirectoryException extends FileSystemException {
    public NotDirectoryException(String file) { super(file); }
}