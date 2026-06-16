package java.util.concurrent;

public class ExecutionException extends Exception {
    public ExecutionException() { super(); }
    public ExecutionException(String message) { super(message); }
    public ExecutionException(Throwable cause) { super(cause); }
    public ExecutionException(String message, Throwable cause) { super(message, cause); }
}
