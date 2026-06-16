package java.util.concurrent.atomic;

import java.io.Serializable;

public class DoubleAdder extends Number implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;
    private double value;

    public DoubleAdder() {}
    public void add(double x) { value += x; }
    public double sum() { return value; }
    public void reset() { value = 0.0; }
    public double sumThenReset() { double v = value; value = 0.0; return v; }

    @Override public int intValue() { return (int) value; }
    @Override public long longValue() { return (long) value; }
    @Override public float floatValue() { return (float) value; }
    @Override public double doubleValue() { return value; }
    @Override public String toString() { return Double.toString(value); }
}
