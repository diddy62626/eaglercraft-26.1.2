package java.sql;

/**
 * TeaVM-compatible stub for java.sql.Date.
 * Browser doesn't use SQL; this is needed by some MC dependencies.
 */
public class Date extends java.util.Date {
    public Date() { super(); }
    public Date(long date) { super(date); }
    public Date(int year, int month, int day) { super(year - 1900, month, day); }

    public static Date valueOf(String s) {
        return new Date(0);
    }

    public String toString() {
        return "0000-00-00";
    }
}
