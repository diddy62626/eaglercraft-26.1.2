package java.lang.management;
public class MemoryUsage {
    private final long init, used, committed, max;
    public MemoryUsage(long init, long used, long committed, long max) {
        this.init = init; this.used = used; this.committed = committed; this.max = max;
    }
    public long getInit() { return init; }
    public long getUsed() { return used; }
    public long getCommitted() { return committed; }
    public long getMax() { return max; }
}