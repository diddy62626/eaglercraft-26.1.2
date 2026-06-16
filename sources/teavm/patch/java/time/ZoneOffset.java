package java.time;
public final class ZoneOffset extends ZoneId {
    public static final ZoneOffset UTC = new ZoneOffset();
    public String getId() { return "Z"; }
}