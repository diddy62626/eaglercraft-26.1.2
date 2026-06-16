package java.util.concurrent;
public class RejectedExecutionException extends RuntimeException {
    public RejectedExecutionException() {}
    public RejectedExecutionException(String message) { super(message); }
}