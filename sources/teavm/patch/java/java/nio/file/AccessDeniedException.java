package java.nio.file;
public class AccessDeniedException extends FileSystemException {
    public AccessDeniedException(String file) { super(file); }
    public AccessDeniedException(String file, String other, String reason) { super(file, other, reason); }
}