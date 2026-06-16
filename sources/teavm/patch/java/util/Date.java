package java.util;

public class Date implements java.io.Serializable, Cloneable, Comparable<Date> {
    private static final long serialVersionUID = 7523967970034938905L;
    private long fastTime;

    public Date() { this(System.currentTimeMillis()); }
    public Date(long date) { fastTime = date; }
    @Deprecated public Date(int year, int month, int date) { this(year, month, date, 0, 0, 0); }
    @Deprecated public Date(int year, int month, int date, int hrs, int min) { this(year, month, date, hrs, min, 0); }
    @Deprecated public Date(int year, int month, int date, int hrs, int min, int sec) {
        fastTime = UTC(year, month, date, hrs, min, sec);
    }
    @Deprecated public Date(String s) { fastTime = parse(s); }

    public Object clone() { return new Date(fastTime); }
    public long getTime() { return fastTime; }
    public void setTime(long time) { fastTime = time; }
    public boolean before(Date when) { return fastTime < when.fastTime; }
    public boolean after(Date when) { return fastTime > when.fastTime; }
    public int compareTo(Date another) { return Long.compare(fastTime, another.fastTime); }
    public int hashCode() { return (int) fastTime ^ (int) (fastTime >>> 32); }
    public boolean equals(Object obj) { return obj instanceof Date && fastTime == ((Date) obj).fastTime; }
    public String toString() { return java.text.DateFormat.getDateTimeInstance().format(this); }

    @Deprecated public int getYear() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.YEAR) - 1900; }
    @Deprecated public int getMonth() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.MONTH); }
    @Deprecated public int getDate() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.DAY_OF_MONTH); }
    @Deprecated public int getDay() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.DAY_OF_WEEK) - 1; }
    @Deprecated public int getHours() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.HOUR_OF_DAY); }
    @Deprecated public int getMinutes() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.MINUTE); }
    @Deprecated public int getSeconds() { return new java.util.GregorianCalendar(this).get(java.util.Calendar.SECOND); }
    @Deprecated public void setYear(int year) {}
    @Deprecated public void setMonth(int month) {}
    @Deprecated public void setDate(int date) {}
    @Deprecated public void setHours(int hours) { fastTime = fastTime; }
    @Deprecated public void setMinutes(int minutes) {}
    @Deprecated public void setSeconds(int seconds) {}
    @Deprecated public long getTimezoneOffset() { return 0; }
    @Deprecated public static long UTC(int year, int month, int date, int hrs, int min, int sec) {
        return new java.util.GregorianCalendar(year + 1900, month, date, hrs, min, sec).getTimeInMillis();
    }
    @Deprecated public static long parse(String s) {
        try {
            return java.text.DateFormat.getDateInstance().parse(s).getTime();
        } catch (java.text.ParseException e) {
            throw new IllegalArgumentException(s, e);
        }
    }
    @Deprecated public String toLocaleString() { return toString(); }
    @Deprecated public String toGMTString() { return toString(); }

    public java.time.Instant toInstant() {
        return java.time.Instant.ofEpochMilli(fastTime);
    }

    public static Date from(java.time.Instant instant) {
        return new Date(instant.toEpochMilli());
    }
}
