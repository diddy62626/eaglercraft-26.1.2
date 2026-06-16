package java.time;
public abstract class Clock {
    public static Clock systemDefaultZone() { return new Clock() { public ZoneId getZone() { return ZoneId.systemDefault(); } public Instant instant() { return Instant.now(); } }; }
    public static Clock system(ZoneId zone) { return systemDefaultZone(); }
    public abstract ZoneId getZone();
    public abstract Instant instant();
    public long millis() { return instant().toEpochMilli(); }
}