package java.util.concurrent;

/**
 * TeaVM-compatible stub for ScheduledFuture.
 */
public interface ScheduledFuture<V> extends Future<V>, java.lang.Comparable<ScheduledFuture<V>> {
    long getDelay(TimeUnit unit);
    @Override default int compareTo(ScheduledFuture<V> o) { return 0; }
}
