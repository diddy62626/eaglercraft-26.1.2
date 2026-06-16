package java.util.concurrent.locks;
public class LockSupport {
    public static void park() {}
    public static void parkNanos(long nanos) {}
    public static void parkUntil(long deadline) {}
    public static void unpark(Thread thread) {}

    public static void park(Object blocker) {
        // No-op stub: TeaVM doesn't have real thread blocking
    }
    public static Object getBlocker(Thread t) {
        return null;
    }
    public static void setCurrentBlocker(Object blocker) {}
    public static void park() {}
    public static void parkNanos(long nanos) {}
    public static void parkNanos(Object blocker, long nanos) {}
    public static void parkUntil(long deadline) {}
    public static void parkUntil(Object blocker, long deadline) {}
    public static void unpark(Thread thread) {}

}
