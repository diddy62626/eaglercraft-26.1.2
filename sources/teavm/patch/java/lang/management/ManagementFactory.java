package java.lang.management;
public class ManagementFactory {
    public static RuntimeMXBean getRuntimeMXBean() { return new RuntimeMXBean(); }
    public static MemoryMXBean getMemoryMXBean() { return new MemoryMXBean(); }
    public static ThreadMXBean getThreadMXBean() { return new ThreadMXBean(); }
    public static java.util.List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() { return java.util.Collections.emptyList(); }
}