package java.nio.file;
public class AtomicMoveNotSupportedException extends FileSystemException {
    public AtomicMoveNotSupportedException(String source, String target, String reason) { super(source, target, reason); }
}