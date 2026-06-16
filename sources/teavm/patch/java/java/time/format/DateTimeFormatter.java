package java.time.format;
public final class DateTimeFormatter {
    public static final DateTimeFormatter ISO_LOCAL_DATE = new DateTimeFormatter();
    public static final DateTimeFormatter ISO_LOCAL_TIME = new DateTimeFormatter();
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME = new DateTimeFormatter();
    public static final DateTimeFormatter ISO_INSTANT = new DateTimeFormatter();
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME = new DateTimeFormatter();
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME = new DateTimeFormatter();
    public static DateTimeFormatter ofPattern(String pattern) { return new DateTimeFormatter(); }
    public static DateTimeFormatter ofPattern(String pattern, java.util.Locale locale) { return new DateTimeFormatter(); }
    public static DateTimeFormatter ofLocalizedDateTime(java.time.format.FormatStyle dateStyle, java.time.format.FormatStyle timeStyle) { return new DateTimeFormatter(); }
    public String format(java.time.temporal.TemporalAccessor temporal) { return ""; }
}