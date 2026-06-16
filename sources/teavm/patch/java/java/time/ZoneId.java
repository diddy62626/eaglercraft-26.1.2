package java.time;
public abstract class ZoneId {
    public static ZoneId systemDefault() { return ZoneOffset.UTC; }
    public static ZoneId of(String zoneId) { return ZoneOffset.UTC; }
    public abstract String getId();
}