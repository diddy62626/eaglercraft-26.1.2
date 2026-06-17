package java.nio.file;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
class DefaultPath implements Path {
    public FileSystem getFileSystem() { return FileSystems.getDefault(); }
    public boolean isAbsolute() { return false; }
    public Path getRoot() { return null; }
    public Path getFileName() { return null; }
    public Path getParent() { return null; }
    public int getNameCount() { return 0; }
    public Path getName(int index) { return null; }
    public Path subpath(int beginIndex, int endIndex) { return null; }
    public boolean startsWith(Path other) { return false; }
    public boolean startsWith(String other) { return false; }
    public boolean endsWith(Path other) { return false; }
    public boolean endsWith(String other) { return false; }
    public Path normalize() { return this; }
    public Path resolve(Path other) { return other; }
    public Path resolve(String other) { return this; }
    public Path resolveSibling(Path other) { return other; }
    public Path resolveSibling(String other) { return this; }
    public Path relativize(Path other) { return other; }
    public java.net.URI toUri() { return java.net.URI.create(""); }
    public Path toAbsolutePath() { return this; }
    public Path toRealPath(LinkOption... options) throws java.io.IOException { return this; }
    public java.io.File toFile() { return new java.io.File(toString()); }
    public java.util.Iterator<Path> iterator() { return java.util.Collections.<Path>emptyList().iterator(); }
    public int compareTo(Path other) { return 0; }
    public String toString() { return ""; }
    public WatchKey register(WatchService watcher, Kind<?>... events) { return null; }
    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) { return null; }
    public String getFileNameString() { return ""; }
}
