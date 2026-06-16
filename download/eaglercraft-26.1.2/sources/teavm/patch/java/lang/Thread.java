package java.lang;

/**
 * TeaVM stub for java.lang.Thread.
 * Browser is single-threaded: Thread.start() is a no-op, sleep returns immediately.
 * Provides a singleton "main thread" for Thread.currentThread().
 */
public class Thread implements Runnable {
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;

    private static final Thread MAIN_THREAD = new Thread("main");
    private static Thread currentThread = MAIN_THREAD;

    static {
        MAIN_THREAD.priority = NORM_PRIORITY;
        MAIN_THREAD.alive = true;
        MAIN_THREAD.daemon = false;
    }

    private String name;
    private int priority;
    private boolean daemon;
    private boolean alive;
    private boolean interrupted;
    private Runnable target;
    private ThreadGroup group;
    private ClassLoader contextClassLoader;
    private long id;
    private static long nextId = 1;

    public Thread() { this(null, null, "Thread-" + nextId++); }
    public Thread(Runnable target) { this(null, target, "Thread-" + nextId++); }
    public Thread(Runnable target, String name) { this(null, target, name); }
    public Thread(String name) { this(null, null, name); }
    public Thread(ThreadGroup group, Runnable target, String name) {
        this.group = group != null ? group : new ThreadGroup("main");
        this.target = target;
        this.name = name;
        this.priority = NORM_PRIORITY;
        this.id = nextId++;
    }
    public Thread(ThreadGroup group, Runnable target, String name, long stackSize) {
        this(group, target, name);
    }
    public Thread(ThreadGroup group, Runnable target, String name, long stackSize, boolean inheritThreadLocals) {
        this(group, target, name);
    }

    public static Thread currentThread() { return currentThread; }

    public static void yield() {}

    public static void sleep(long millis) throws InterruptedException {
        // No real sleeping in browser - return immediately
    }

    public static void sleep(long millis, int nanos) throws InterruptedException {
        sleep(millis);
    }

    public static boolean interrupted() {
        boolean was = currentThread.interrupted;
        currentThread.interrupted = false;
        return was;
    }

    public void start() {
        // In browser, we can't create real threads.
        // Just run the target synchronously if possible.
        alive = true;
        if (target != null) {
            try { target.run(); } catch (Throwable t) { t.printStackTrace(); }
        }
    }

    public void run() {
        if (target != null) target.run();
    }

    public void interrupt() { interrupted = true; }

    public boolean isInterrupted() { return interrupted; }

    public boolean isAlive() { return alive; }

    public void setPriority(int newPriority) {
        if (newPriority < MIN_PRIORITY || newPriority > MAX_PRIORITY) throw new IllegalArgumentException();
        this.priority = newPriority;
    }

    public int getPriority() { return priority; }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public ThreadGroup getThreadGroup() { return group; }

    public static int activeCount() { return 1; }

    public static int enumerate(Thread[] tarray) {
        if (tarray.length > 0) tarray[0] = MAIN_THREAD;
        return 1;
    }

    public static void dumpStack() {}

    public long getId() { return id; }

    public Thread.State getState() {
        return alive ? State.RUNNABLE : State.NEW;
    }

    public void setDaemon(boolean on) { this.daemon = on; }
    public boolean isDaemon() { return daemon; }

    public void setContextClassLoader(ClassLoader cl) { this.contextClassLoader = cl; }
    public ClassLoader getContextClassLoader() { return contextClassLoader; }

    public static boolean holdsLock(Object obj) { return false; }

    public StackTraceElement[] getStackTrace() { return new StackTraceElement[0]; }

    public String toString() { return "Thread[" + name + "," + priority + "," + (group != null ? group.getName() : "") + "]"; }

    public enum State {
        NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
    }

    public static class UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {}
    }

    private UncaughtExceptionHandler uncaughtExceptionHandler;
    private static UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    public UncaughtExceptionHandler getUncaughtExceptionHandler() { return uncaughtExceptionHandler; }
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) { this.uncaughtExceptionHandler = eh; }
    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() { return defaultUncaughtExceptionHandler; }
    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler eh) { defaultUncaughtExceptionHandler = eh; }

    public void join() throws InterruptedException {}
    public void join(long millis) throws InterruptedException {}
    public void join(long millis, int nanos) throws InterruptedException {}

    public static void onSpinWait() {}

    public ClassLoader getContextClassLoader(ClassLoader cl) { return contextClassLoader; }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
