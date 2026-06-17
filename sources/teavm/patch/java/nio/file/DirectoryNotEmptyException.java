package java.nio.file;
public class DirectoryNotEmptyException extends FileSystemException {
    public DirectoryNotEmptyException(String dir) { super(dir); }
}