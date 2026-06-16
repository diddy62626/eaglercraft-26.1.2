package java.text;
public class SimpleDateFormat extends DateFormat {
    public SimpleDateFormat() {}
    public SimpleDateFormat(String pattern) {}
    public SimpleDateFormat(String pattern, java.util.Locale locale) {}
    public String format(java.util.Date date) { return date.toString(); }
    public java.util.Date parse(String source) throws ParseException { return new java.util.Date(); }
}