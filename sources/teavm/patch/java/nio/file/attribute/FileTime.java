package java.nio.file.attribute;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class FileTime implements Comparable<FileTime> {
    private final long millis;

    private FileTime(long millis) {
        this.millis = millis;
    }

    public static FileTime from(long value, TimeUnit unit) {
        // Simple conversion: assume value is in given unit, convert to millis
        if (unit == TimeUnit.SECONDS) return new FileTime(value * 1000L);
        if (unit == TimeUnit.MILLISECONDS) return new FileTime(value);
        if (unit == TimeUnit.MICROSECONDS) return new FileTime(value / 1000L);
        if (unit == TimeUnit.NANOSECONDS) return new FileTime(value / 1_000_000L);
        if (unit == TimeUnit.MINUTES) return new FileTime(value * 60_000L);
        if (unit == TimeUnit.HOURS) return new FileTime(value * 3_600_000L);
        if (unit == TimeUnit.DAYS) return new FileTime(value * 86_400_000L);
        return new FileTime(value);
    }

    public static FileTime fromMillis(long value) {
        return new FileTime(value);
    }

    public static FileTime from(Instant instant) {
        return new FileTime(instant.toEpochMilli());
    }

    public long to(TimeUnit unit) {
        if (unit == TimeUnit.SECONDS) return millis / 1000L;
        if (unit == TimeUnit.MILLISECONDS) return millis;
        if (unit == TimeUnit.MICROSECONDS) return millis * 1000L;
        if (unit == TimeUnit.NANOSECONDS) return millis * 1_000_000L;
        if (unit == TimeUnit.MINUTES) return millis / 60_000L;
        if (unit == TimeUnit.HOURS) return millis / 3_600_000L;
        if (unit == TimeUnit.DAYS) return millis / 86_400_000L;
        return millis;
    }

    public long toMillis() {
        return millis;
    }

    public Instant toInstant() {
        return Instant.ofEpochMilli(millis);
    }

    public boolean equals(Object obj) {
        return obj instanceof FileTime && compareTo((FileTime) obj) == 0;
    }

    public int hashCode() { return Long.hashCode(millis); }

    public int compareTo(FileTime other) {
        return Long.compare(millis, other.millis);
    }

    public String toString() {
        return "FileTime[" + millis + "]";
    }
}
