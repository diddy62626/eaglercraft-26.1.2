package java.util.concurrent.atomic;

import java.io.Serializable;

public class LongAdder extends Number implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    private long value;

    public LongAdder() {}

    public void increment() { value++; }
    public void decrement() { value--; }
    public void add(long x) { value += x; }
    public long sum() { return value; }
    public void reset() { value = 0L; }
    public long sumThenReset() { long v = value; value = 0L; return v; }

    @Override public int intValue() { return (int) value; }
    @Override public long longValue() { return value; }
    @Override public float floatValue() { return (float) value; }
    @Override public double doubleValue() { return (double) value; }

    @Override public String toString() { return Long.toString(value); }
    @Override public boolean equals(Object obj) {
        if (!(obj instanceof LongAdder)) return false;
        return value == ((LongAdder) obj).value;
    }
    @Override public int hashCode() { return Long.hashCode(value); }
}
