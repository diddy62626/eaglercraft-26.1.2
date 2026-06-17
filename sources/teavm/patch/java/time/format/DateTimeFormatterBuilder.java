package java.time.format;
public class DateTimeFormatterBuilder {
    public DateTimeFormatterBuilder() {}
    public DateTimeFormatterBuilder appendValue(java.time.temporal.TemporalField field) { return this; }
    public DateTimeFormatterBuilder appendLiteral(char c) { return this; }
    public DateTimeFormatterBuilder appendPattern(String pattern) { return this; }
    public DateTimeFormatter toFormatter() { return new DateTimeFormatter(); }
}