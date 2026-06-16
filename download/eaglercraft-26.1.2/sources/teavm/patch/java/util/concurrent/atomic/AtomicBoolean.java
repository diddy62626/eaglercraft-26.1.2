package java.util.concurrent.atomic;

/**
 * TeaVM stub for java.util.concurrent.atomic.AtomicBoolean.
 * Browser is single-threaded; volatile semantics are no-ops.
 */
public class AtomicBoolean implements java.io.Serializable {

    private volatile boolean value;

    public AtomicBoolean() {
    }

    public AtomicBoolean(boolean initialValue) {
        this.value = initialValue;
    }

    public final boolean get() {
        return value;
    }

    public final void set(boolean newValue) {
        value = newValue;
    }

    public final void lazySet(boolean newValue) {
        value = newValue;
    }

    public final boolean compareAndSet(boolean expectedValue, boolean newValue) {
        if (value == expectedValue) {
            value = newValue;
            return true;
        }
        return false;
    }

    public final boolean weakCompareAndSet(boolean expectedValue, boolean newValue) {
        return compareAndSet(expectedValue, newValue);
    }

    public final boolean getAndSet(boolean newValue) {
        boolean old = value;
        value = newValue;
        return old;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
