package java.time;
public final class ZonedDateTime {
    public static ZonedDateTime now() { return new ZonedDateTime(); }
    public static ZonedDateTime now(ZoneId zone) { return new ZonedDateTime(); }
    public static ZonedDateTime ofInstant(Instant instant, ZoneId zone) { return new ZonedDateTime(); }
    public int getYear() { return 0; }
    public int getMonthValue() { return 1; }
    public int getDayOfMonth() { return 1; }
    public int getHour() { return 0; }
    public int getMinute() { return 0; }
    public int getSecond() { return 0; }
    public ZoneId getZone() { return ZoneOffset.UTC; }
    public Instant toInstant() { return Instant.now(); }
}