package java.nio.file;
public class FileSystemException extends java.io.IOException {
    private final String file, other, reason;
    public FileSystemException(String file) { this(file, null, null); }
    public FileSystemException(String file, String other, String reason) { super(file); this.file = file; this.other = other; this.reason = reason; }
    public String getFile() { return file; }
    public String getOtherFile() { return other; }
    public String getReason() { return reason; }
}