package java.text;
public abstract class NumberFormat {
    public static NumberFormat getInstance() { return new DecimalFormat(); }
    public static NumberFormat getInstance(java.util.Locale inLocale) { return new DecimalFormat(); }
    public static NumberFormat getNumberInstance() { return new DecimalFormat(); }
    public static NumberFormat getIntegerInstance() { return new DecimalFormat(); }
    public static NumberFormat getPercentInstance() { return new DecimalFormat(); }
    public static NumberFormat getCurrencyInstance() { return new DecimalFormat(); }
    public final String format(double number) { return Double.toString(number); }
    public final String format(long number) { return Long.toString(number); }
    public abstract StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos);
    public abstract StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos);
    public Number parse(String source) throws ParseException { return 0; }
    public abstract Number parse(String source, ParsePosition pos);
}