package java.util;
public class IntSummaryStatistics {
    public void accept(int value) {}
    public long getCount() { return 0; }
    public long getSum() { return 0; }
    public double getAverage() { return 0; }
    public int getMin() { return Integer.MAX_VALUE; }
    public int getMax() { return Integer.MIN_VALUE; }
}