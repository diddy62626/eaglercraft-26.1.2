package java.nio.file;
public class InvalidPathException extends IllegalArgumentException {
    public InvalidPathException(String input, String reason) { super(reason); }
    public InvalidPathException(String input, String reason, int index) { super(reason); }
}