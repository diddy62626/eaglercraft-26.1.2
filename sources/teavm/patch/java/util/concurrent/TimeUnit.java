package java.util.concurrent;
public enum TimeUnit {
    NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS;
    public long toMillis(long duration) { return duration; }
    public long toSeconds(long duration) { return duration / 1000; }
    public long toMinutes(long duration) { return duration / 60000; }
    public void sleep(long timeout) throws InterruptedException {}
}