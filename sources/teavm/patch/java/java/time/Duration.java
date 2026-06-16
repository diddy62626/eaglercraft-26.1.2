package java.time;
import java.time.temporal.Temporal;
public final class Duration {
    public static Duration ofMillis(long millis) { return new Duration(); }
    public static Duration ofSeconds(long seconds) { return new Duration(); }
    public static Duration ofSeconds(long seconds, long nanoAdjustment) { return new Duration(); }
    public static Duration ofMinutes(long minutes) { return new Duration(); }
    public static Duration ofHours(long hours) { return new Duration(); }
    public static Duration between(Temporal startInclusive, Temporal endExclusive) { return new Duration(); }
    public long toMillis() { return 0; }
    public long getSeconds() { return 0; }
    public int getNano() { return 0; }
}