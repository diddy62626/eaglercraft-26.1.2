package java.lang.management;
public class MemoryMXBean {
    public MemoryUsage getHeapMemoryUsage() { return new MemoryUsage(0, 0, 0, 0); }
    public MemoryUsage getNonHeapMemoryUsage() { return new MemoryUsage(0, 0, 0, 0); }
}