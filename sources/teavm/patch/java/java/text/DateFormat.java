package java.text;
public abstract class DateFormat {
    public static final int DEFAULT = 2;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    public abstract String format(java.util.Date date);
}