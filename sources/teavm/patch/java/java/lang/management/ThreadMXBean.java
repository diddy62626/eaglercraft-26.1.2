package java.lang.management;
public class ThreadMXBean {
    public int getThreadCount() { return 1; }
    public int getPeakThreadCount() { return 1; }
    public long getTotalStartedThreadCount() { return 1; }
    public ThreadInfo getThreadInfo(long id) { return new ThreadInfo(); }
    public ThreadInfo[] getThreadInfo(long[] ids) { return new ThreadInfo[0]; }
    public boolean isThreadContentionMonitoringSupported() { return false; }
    public boolean isThreadCpuTimeSupported() { return false; }
}