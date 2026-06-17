package java.util.concurrent;

/**
 * TeaVM-compatible stub for Callable.
 */
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
