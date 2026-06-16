package java.util.concurrent.atomic;

/**
 * TeaVM stub for java.util.concurrent.atomic.AtomicReference.
 * Browser is single-threaded; volatile semantics are no-ops.
 */
public class AtomicReference<V> implements java.io.Serializable {

    private volatile V value;

    public AtomicReference() {
    }

    public AtomicReference(V initialValue) {
        this.value = initialValue;
    }

    public final V get() {
        return value;
    }

    public final void set(V newValue) {
        value = newValue;
    }

    public final void lazySet(V newValue) {
        value = newValue;
    }

    public final boolean compareAndSet(V expectedValue, V newValue) {
        if (value == expectedValue) {
            value = newValue;
            return true;
        }
        return false;
    }

    public final boolean weakCompareAndSet(V expectedValue, V newValue) {
        return compareAndSet(expectedValue, newValue);
    }

    @SuppressWarnings("unchecked")
    public final V getAndSet(V newValue) {
        V old = value;
        value = newValue;
        return old;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
