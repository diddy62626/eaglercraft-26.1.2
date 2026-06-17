package java.nio.file;
public class DirectoryIteratorException extends FileSystemException {
    public DirectoryIteratorException(java.io.IOException cause) { super("", null, null); initCause(cause); }
}