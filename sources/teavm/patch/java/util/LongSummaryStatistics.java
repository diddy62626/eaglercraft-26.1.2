package java.util;
public class LongSummaryStatistics {
    public void accept(long value) {}
    public long getCount() { return 0; }
    public long getSum() { return 0; }
    public double getAverage() { return 0; }
    public long getMin() { return Long.MAX_VALUE; }
    public long getMax() { return Long.MIN_VALUE; }
}