package java.util.concurrent.atomic;

/**
 * TeaVM-compatible stub for AtomicIntegerArray.
 */
public class AtomicIntegerArray implements java.io.Serializable {
    private final int[] array;

    public AtomicIntegerArray(int length) { this.array = new int[length]; }
    public AtomicIntegerArray(int[] array) { this.array = array.clone(); }

    public int get(int i) { return array[i]; }
    public void set(int i, int newValue) { array[i] = newValue; }
    public void lazySet(int i, int newValue) { array[i] = newValue; }
    public int getAndSet(int i, int newValue) { int old = array[i]; array[i] = newValue; return old; }
    public boolean compareAndSet(int i, int expectedValue, int newValue) {
        if (array[i] == expectedValue) { array[i] = newValue; return true; }
        return false;
    }
    public int getAndIncrement(int i) { return array[i]++; }
    public int getAndDecrement(int i) { return array[i]--; }
    public int getAndAdd(int i, int delta) { int old = array[i]; array[i] += delta; return old; }
    public int incrementAndGet(int i) { return ++array[i]; }
    public int decrementAndGet(int i) { return --array[i]; }
    public int addAndGet(int i, int delta) { array[i] += delta; return array[i]; }
    public int length() { return array.length; }
    public String toString() { return java.util.Arrays.toString(array); }
}
