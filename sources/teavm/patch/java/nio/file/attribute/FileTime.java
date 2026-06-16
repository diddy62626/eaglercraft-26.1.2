package java.nio.file.attribute;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public final class FileTime implements Comparable<FileTime> {
    private final long value;
    private final TimeUnit unit;

    private FileTime(long value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static FileTime from(long value, TimeUnit unit) {
        return new FileTime(value, unit);
    }

    public static FileTime fromMillis(long value) {
        return new FileTime(value, TimeUnit.MILLISECONDS);
    }

    public static FileTime from(Instant instant) {
        return new FileTime(instant.toEpochMilli(), TimeUnit.MILLISECONDS);
    }

    public long to(TimeUnit unit) {
        return unit.convert(value, this.unit);
    }

    public long toMillis() {
        return TimeUnit.MILLISECONDS.convert(value, unit);
    }

    public Instant toInstant() {
        return Instant.ofEpochMilli(toMillis());
    }

    public boolean equals(Object obj) {
        return obj instanceof FileTime && compareTo((FileTime) obj) == 0;
    }

    public int hashCode() { return Long.hashCode(value); }

    public int compareTo(FileTime other) {
        return Long.compare(toMillis(), other.toMillis());
    }

    public String toString() {
        return "FileTime[" + toMillis() + "]";
    }
}
