package java.util;

public class Date implements java.io.Serializable, Cloneable, Comparable<Date> {
    private static final long serialVersionUID = 7523967970034938905L;
    private long fastTime;

    public Date() { this(System.currentTimeMillis()); }
    public Date(long date) { fastTime = date; }
    public Date(int year, int month, int date) { this(year, month, date, 0, 0, 0); }
    public Date(int year, int month, int date, int hrs, int min) { this(year, month, date, hrs, min, 0); }
    public Date(int year, int month, int date, int hrs, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(year + 1900, month, date, hrs, min, sec);
        fastTime = cal.getTimeInMillis();
    }
    public Date(String s) {
        // Simple parser: just set fastTime to 0 (browser stub)
        fastTime = 0;
    }

    public Object clone() { return new Date(fastTime); }
    public long getTime() { return fastTime; }
    public void setTime(long time) { fastTime = time; }
    public boolean before(Date when) { return fastTime < when.fastTime; }
    public boolean after(Date when) { return fastTime > when.fastTime; }
    public int compareTo(Date another) { return Long.compare(fastTime, another.fastTime); }
    public int hashCode() { return (int) fastTime ^ (int) (fastTime >>> 32); }
    public boolean equals(Object obj) { return obj instanceof Date && fastTime == ((Date) obj).fastTime; }

    public String toString() {
        // Simple RFC-like format
        return "Date[" + fastTime + "]";
    }

    public int getYear() { return 0; }
    public int getMonth() { return 0; }
    public int getDate() { return 1; }
    public int getDay() { return 0; }
    public int getHours() { return 0; }
    public int getMinutes() { return 0; }
    public int getSeconds() { return 0; }
    public void setYear(int year) {}
    public void setMonth(int month) {}
    public void setDate(int date) {}
    public void setHours(int hours) {}
    public void setMinutes(int minutes) {}
    public void setSeconds(int seconds) {}
    public long getTimezoneOffset() { return 0; }
    public static long UTC(int year, int month, int date, int hrs, int min, int sec) {
        Calendar cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        cal.set(year + 1900, month, date, hrs, min, sec);
        return cal.getTimeInMillis();
    }
    public static long parse(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public String toLocaleString() { return toString(); }
    public String toGMTString() { return toString(); }

    public java.time.Instant toInstant() {
        return java.time.Instant.ofEpochMilli(fastTime);
    }

    public static Date from(java.time.Instant instant) {
        return new Date(instant.toEpochMilli());
    }
}
