package java.util;
public class DoubleSummaryStatistics {
    public void accept(double value) {}
    public long getCount() { return 0; }
    public double getSum() { return 0; }
    public double getAverage() { return 0; }
    public double getMin() { return Double.MAX_VALUE; }
    public double getMax() { return Double.MIN_VALUE; }
}