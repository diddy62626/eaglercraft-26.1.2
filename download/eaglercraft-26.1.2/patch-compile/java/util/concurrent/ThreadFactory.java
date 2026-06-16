package java.util.concurrent;

/**
 * TeaVM-compatible stub for ThreadFactory.
 */
@FunctionalInterface
public interface ThreadFactory {
    Thread newThread(Runnable r);
}
