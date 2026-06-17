package java.sql;

import java.time.Instant;
import java.util.Date;

/**
 * TeaVM stub for java.sql.Timestamp.
 */
public class Timestamp extends Date {
    private static final long serialVersionUID = 2745172702276190257L;
    private int nanos;

    public Timestamp(long time) {
        super(time);
    }

    public void setTime(long time) {
        super.setTime(time);
    }

    public long getTime() { return super.getTime(); }
    public int getNanos() { return nanos; }
    public void setNanos(int n) { this.nanos = n; }

    public boolean equals(Timestamp ts) {
        return ts != null && getTime() == ts.getTime() && nanos == ts.nanos;
    }

    public boolean before(Timestamp ts) { return getTime() < ts.getTime(); }
    public boolean after(Timestamp ts) { return getTime() > ts.getTime(); }

    public int compareTo(Timestamp ts) {
        long diff = getTime() - ts.getTime();
        if (diff != 0) return diff < 0 ? -1 : 1;
        return nanos - ts.nanos;
    }

    public static Timestamp valueOf(String s) {
        return new Timestamp(0);
    }

    public static Timestamp from(Instant instant) {
        return new Timestamp(instant.toEpochMilli());
    }

    public Instant toInstant() { return Instant.ofEpochMilli(getTime()); }

    public String toString() { return "1970-01-01 00:00:00.0"; }
}
