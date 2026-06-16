package java.sql;

/**
 * TeaVM stub for java.sql.Time.
 * Subclass of java.util.Date with HH:MM:SS formatting.
 */
public class Time extends java.util.Date {
    public Time(long time) {
        super(time);
    }

    public Time(int hour, int minute, int second) {
        super(70, 0, 1, hour, minute, second);
    }

    public void setTime(long time) {
        super.setTime(time);
    }

    public String toString() {
        int hour = getHours();
        int minute = getMinutes();
        int second = getSeconds();
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @SuppressWarnings("deprecation")
    public static Time valueOf(String s) {
        if (s == null) throw new IllegalArgumentException();
        String[] parts = s.split(":");
        if (parts.length != 3) throw new IllegalArgumentException();
        return new Time(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
