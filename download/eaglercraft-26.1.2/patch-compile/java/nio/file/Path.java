package java.nio.file;

import java.io.File;

/**
 * TeaVM-compatible stub for java.nio.file.Path.
 * Browser filesystem access goes through IndexedDB, not real file paths.
 */
public interface Path extends Comparable<Path>, Iterable<Path> {
    static Path of(String first, String... more) {
        return new StubPath(first);
    }

    static Path of(java.net.URI uri) {
        return new StubPath(uri.toString());
    }

    Path resolve(String other);
    Path resolve(Path other);
    Path getParent();
    Path getFileName();
    Path getRoot();
    String getFileNameString();
    boolean isAbsolute();
    Path relativize(Path other);
    Path normalize();
    String toString();
}

class StubPath implements Path {
    private final String path;

    StubPath(String path) { this.path = path; }

    @Override public int compareTo(Path o) { return path.compareTo(o.toString()); }
    @Override public java.util.Iterator<Path> iterator() { return java.util.Collections.emptyIterator(); }
    @Override public Path resolve(String other) { return new StubPath(path + "/" + other); }
    @Override public Path resolve(Path other) { return new StubPath(path + "/" + other.toString()); }
    @Override public Path getParent() { return new StubPath(path); }
    @Override public Path getFileName() { return this; }
    @Override public Path getRoot() { return this; }
    @Override public String getFileNameString() { return path; }
    @Override public boolean isAbsolute() { return path.startsWith("/"); }
    @Override public Path relativize(Path other) { return other; }
    @Override public Path normalize() { return this; }
    @Override public String toString() { return path; }
    @Override public boolean equals(Object o) { return o instanceof Path && path.equals(o.toString()); }
    @Override public int hashCode() { return path.hashCode(); }
}
