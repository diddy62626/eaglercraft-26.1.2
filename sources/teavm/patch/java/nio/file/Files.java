package java.nio.file;
import java.nio.file.attribute.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiPredicate;
public final class Files {
    public static InputStream newInputStream(Path path, OpenOption... options) throws java.io.IOException { return new java.io.ByteArrayInputStream(new byte[0]); }
    public static OutputStream newOutputStream(Path path, OpenOption... options) throws java.io.IOException { return new java.io.ByteArrayOutputStream(); }
    public static DirectoryStream<Path> newDirectoryStream(Path dir) throws java.io.IOException { return new DirectoryStream<Path>() { public java.util.Iterator<Path> iterator() { return java.util.Collections.<Path>emptyList().iterator(); } public void close() {} }; }
    public static DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws java.io.IOException { return newDirectoryStream(dir); }
    public static boolean exists(Path path, LinkOption... options) { return false; }
    public static boolean notExists(Path path, LinkOption... options) { return true; }
    public static boolean isDirectory(Path path, LinkOption... options) { return false; }
    public static boolean isRegularFile(Path path, LinkOption... options) { return false; }
    public static boolean isReadable(Path path) { return false; }
    public static boolean isWritable(Path path) { return false; }
    public static boolean isHidden(Path path) { return false; }
    public static boolean isSymbolicLink(Path path) { return false; }
    public static long size(Path path) throws java.io.IOException { return 0; }
    public static Path createFile(Path path, FileAttribute<?>... attrs) throws java.io.IOException { return path; }
    public static Path createDirectory(Path dir, FileAttribute<?>... attrs) throws java.io.IOException { return dir; }
    public static Path createDirectories(Path dir, FileAttribute<?>... attrs) throws java.io.IOException { return dir; }
    public static Path createTempFile(String prefix, String suffix, FileAttribute<?>... attrs) throws java.io.IOException { return new DefaultPath(); }
    public static Path createTempDirectory(String prefix, FileAttribute<?>... attrs) throws java.io.IOException { return new DefaultPath(); }
    public static void delete(Path path) throws java.io.IOException {}
    public static boolean deleteIfExists(Path path) throws java.io.IOException { return false; }
    public static Path copy(Path source, Path target, CopyOption... options) throws java.io.IOException { return target; }
    public static Path move(Path source, Path target, CopyOption... options) throws java.io.IOException { return target; }
    public static Path write(Path path, byte[] bytes, OpenOption... options) throws java.io.IOException { return path; }
    public static Path write(Path path, Iterable<? extends CharSequence> lines, java.nio.charset.Charset cs, OpenOption... options) throws java.io.IOException { return path; }
    public static byte[] readAllBytes(Path path) throws java.io.IOException { return new byte[0]; }
    public static java.util.List<String> readAllLines(Path path) throws java.io.IOException { return java.util.Collections.emptyList(); }
    public static java.util.List<String> readAllLines(Path path, java.nio.charset.Charset cs) throws java.io.IOException { return java.util.Collections.emptyList(); }
    public static BasicFileAttributes readAttributes(Path path, Class<BasicFileAttributes> type, LinkOption... options) throws java.io.IOException { return null; }
    public static Object getAttribute(Path path, String attribute, LinkOption... options) throws java.io.IOException { return null; }
    public static void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws java.io.IOException {}
    public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) throws java.io.IOException { return start; }
    public static Path walkFileTree(Path start, java.util.Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) throws java.io.IOException { return start; }
    public static java.util.stream.Stream<Path> walk(Path start, FileVisitOption... options) { return java.util.stream.Stream.empty(); }
    public static java.util.stream.Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) { return java.util.stream.Stream.empty(); }
    public static java.util.stream.Stream<Path> list(Path dir) throws java.io.IOException { return java.util.stream.Stream.empty(); }
    public static java.util.stream.Stream<Path> find(Path start, int maxDepth, BiPredicate<Path, BasicFileAttributes> matcher, FileVisitOption... options) { return java.util.stream.Stream.empty(); }

    public static FileStore getFileStore(Path path) throws IOException {
        return null;
    }

}
