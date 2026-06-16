package java.util.concurrent.atomic;

/**
 * TeaVM-compatible stub for AtomicLongArray.
 */
public class AtomicLongArray implements java.io.Serializable {
    private final long[] array;

    public AtomicLongArray(int length) { this.array = new long[length]; }
    public AtomicLongArray(long[] array) { this.array = array.clone(); }

    public long get(int i) { return array[i]; }
    public void set(int i, long newValue) { array[i] = newValue; }
    public void lazySet(int i, long newValue) { array[i] = newValue; }
    public long getAndSet(int i, long newValue) { long old = array[i]; array[i] = newValue; return old; }
    public boolean compareAndSet(int i, long expectedValue, long newValue) {
        if (array[i] == expectedValue) { array[i] = newValue; return true; }
        return false;
    }
    public long getAndIncrement(int i) { return array[i]++; }
    public long getAndDecrement(int i) { return array[i]--; }
    public long getAndAdd(int i, long delta) { long old = array[i]; array[i] += delta; return old; }
    public long incrementAndGet(int i) { return ++array[i]; }
    public long decrementAndGet(int i) { return --array[i]; }
    public long addAndGet(int i, long delta) { array[i] += delta; return array[i]; }
    public int length() { return array.length; }
    public String toString() { return java.util.Arrays.toString(array); }
}
