package java.util.concurrent.atomic;

/**
 * TeaVM stub for java.util.concurrent.atomic.AtomicLong.
 * Browser is single-threaded; volatile semantics are no-ops.
 */
public class AtomicLong extends Number implements java.io.Serializable {

    private volatile long value;

    public AtomicLong() {
    }

    public AtomicLong(long initialValue) {
        this.value = initialValue;
    }

    public final long get() {
        return value;
    }

    public final void set(long newValue) {
        value = newValue;
    }

    public final void lazySet(long newValue) {
        value = newValue;
    }

    public final boolean compareAndSet(long expectedValue, long newValue) {
        if (value == expectedValue) {
            value = newValue;
            return true;
        }
        return false;
    }

    public final boolean weakCompareAndSet(long expectedValue, long newValue) {
        return compareAndSet(expectedValue, newValue);
    }

    public final long getAndSet(long newValue) {
        long old = value;
        value = newValue;
        return old;
    }

    public final long getAndIncrement() {
        return value++;
    }

    public final long getAndDecrement() {
        return value--;
    }

    public final long getAndAdd(long delta) {
        long old = value;
        value += delta;
        return old;
    }

    public final long incrementAndGet() {
        return ++value;
    }

    public final long decrementAndGet() {
        return --value;
    }

    public final long addAndGet(long delta) {
        value += delta;
        return value;
    }

    public final long getAndUpdate(java.util.function.LongUnaryOperator updateFunction) {
        long prev = get();
        set(updateFunction.applyAsLong(prev));
        return prev;
    }

    public final long updateAndGet(java.util.function.LongUnaryOperator updateFunction) {
        long next = updateFunction.applyAsLong(get());
        set(next);
        return next;
    }

    public final long getAndAccumulate(long x, java.util.function.LongBinaryOperator accumulatorFunction) {
        long prev = get();
        set(accumulatorFunction.applyAsLong(prev, x));
        return prev;
    }

    public final long accumulateAndGet(long x, java.util.function.LongBinaryOperator accumulatorFunction) {
        long next = accumulatorFunction.applyAsLong(get(), x);
        set(next);
        return next;
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }
}
