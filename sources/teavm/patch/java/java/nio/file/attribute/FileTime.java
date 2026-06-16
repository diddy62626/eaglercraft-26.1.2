package java.nio.file.attribute;
public final class FileTime implements Comparable<FileTime> {
    public static FileTime fromMillis(long value) { return new FileTime(); }
    public static FileTime from(java.time.Instant instant) { return new FileTime(); }
    public long toMillis() { return 0; }
    public int compareTo(FileTime other) { return 0; }
}