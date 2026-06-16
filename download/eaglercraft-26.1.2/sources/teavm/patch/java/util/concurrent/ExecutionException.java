package java.util.concurrent;

/**
 * TeaVM-compatible stub for ExecutionException.
 * Thrown when attempting to retrieve the result of a task that aborted
 * due to an exception.
 */
public class ExecutionException extends Exception {
    public ExecutionException() {
        super();
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
