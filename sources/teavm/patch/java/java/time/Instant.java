package java.time;
public final class Instant {
    public static Instant now() { return new Instant(); }
    public static Instant ofEpochMilli(long epochMilli) { return new Instant(); }
    public static Instant ofEpochSecond(long epochSecond) { return new Instant(); }
    public long toEpochMilli() { return System.currentTimeMillis(); }
    public long getEpochSecond() { return 0; }
    public int getNano() { return 0; }
    public boolean isAfter(Instant other) { return false; }
    public boolean isBefore(Instant other) { return false; }
    public Duration durationUntil(Instant end) { return Duration.ofMillis(0); }
}