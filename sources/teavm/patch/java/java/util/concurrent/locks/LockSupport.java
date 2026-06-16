package java.util.concurrent.locks;
public class LockSupport {
    public static void park() {}
    public static void parkNanos(long nanos) {}
    public static void parkUntil(long deadline) {}
    public static void unpark(Thread thread) {}
}