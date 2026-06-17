package java.nio.file;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.Writer;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * TeaVM stub for java.nio.file.Files.
 * Browser: file system access is via IndexedDB; this is a minimal stub.
 */
public final class Files {
    private Files() {}

    public static InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        return new java.io.ByteArrayInputStream(new byte[0]);
    }

    public static OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
        return new java.io.ByteArrayOutputStream();
    }

    public static BufferedReader newBufferedReader(Path path, Charset cs) throws IOException {
        return null;
    }

    public static BufferedWriter newBufferedWriter(Path path, Charset cs, OpenOption... options) throws IOException {
        return null;
    }

    public static byte[] readAllBytes(Path path) throws IOException {
        return new byte[0];
    }

    public static String readString(Path path, Charset cs) throws IOException {
        return "";
    }

    public static List<String> readAllLines(Path path, Charset cs) throws IOException {
        return new ArrayList<>();
    }

    public static List<String> readAllLines(Path path) throws IOException {
        return readAllLines(path, Charset.defaultCharset());
    }

    public static Path write(Path path, byte[] bytes, OpenOption... options) throws IOException {
        return path;
    }

    public static Path write(Path path, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException {
        return path;
    }

    public static Path write(Path path, Iterable<? extends CharSequence> lines, OpenOption... options) throws IOException {
        return write(path, lines, Charset.defaultCharset(), options);
    }

    public static Path writeString(Path path, CharSequence csq, Charset cs, OpenOption... options) throws IOException {
        return path;
    }

    public static Path writeString(Path path, CharSequence csq, OpenOption... options) throws IOException {
        return writeString(path, csq, Charset.defaultCharset(), options);
    }

    public static Path createFile(Path path, FileAttribute<?>... attrs) throws IOException {
        return path;
    }

    public static Path createDirectory(Path path, FileAttribute<?>... attrs) throws IOException {
        return path;
    }

    public static Path createDirectories(Path path, FileAttribute<?>... attrs) throws IOException {
        return path;
    }

    public static void delete(Path path) throws IOException {}
    public static boolean deleteIfExists(Path path) throws IOException { return true; }

    public static boolean exists(Path path, LinkOption... options) { return false; }
    public static boolean notExists(Path path, LinkOption... options) { return true; }
    public static boolean isDirectory(Path path, LinkOption... options) { return false; }
    public static boolean isRegularFile(Path path, LinkOption... options) { return false; }
    public static boolean isReadable(Path path) { return false; }
    public static boolean isWritable(Path path) { return false; }
    public static boolean isExecutable(Path path) { return false; }
    public static boolean isHidden(Path path) throws IOException { return false; }

    public static boolean isSymbolicLink(Path path) { return false; }

    public static long size(Path path) throws IOException { return 0L; }

    public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
        return target;
    }

    public static Path move(Path source, Path target, CopyOption... options) throws IOException {
        return target;
    }

    public static Stream<Path> list(Path dir) throws IOException {
        return Stream.empty();
    }

    public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException {
        return Stream.empty();
    }

    public static Stream<Path> walk(Path start, FileVisitOption... options) throws IOException {
        return walk(start, Integer.MAX_VALUE, options);
    }

    public static Stream<Path> find(Path start, int maxDepth, java.util.function.BiPredicate<Path, java.nio.file.attribute.BasicFileAttributes> matcher, FileVisitOption... options) throws IOException {
        return Stream.empty();
    }

    public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor) {
        return start;
    }

    public static Path walkFileTree(Path start, java.util.Set<FileVisitOption> options, int maxDepth, FileVisitor<? super Path> visitor) {
        return start;
    }

    public static Object getAttribute(Path path, String attribute, LinkOption... options) throws IOException {
        return null;
    }

    public static Path setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        return path;
    }

    public static java.util.Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return new java.util.HashMap<>();
    }

    public static <A extends java.nio.file.attribute.BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return null;
    }

    public static FileStore getFileStore(Path path) throws IOException {
        return null;
    }

    public static Object getFileAttributeView(Path path, Class<? extends java.nio.file.attribute.FileAttributeView> type, LinkOption... options) {
        return null;
    }

    public static boolean isSameFile(Path path, Path path2) throws IOException {
        return path.equals(path2);
    }

    public static long copy(InputStream in, Path target, CopyOption... options) throws IOException {
        return 0L;
    }

    public static long copy(Path source, OutputStream out) throws IOException {
        return 0L;
    }

    public static long lines(Path path) throws IOException {
        return 0L;
    }

    public static Stream<String> lines(Path path, Charset cs) throws IOException {
        return Stream.empty();
    }

    public static Path createLink(Path link, Path existing) throws IOException {
        return link;
    }

    public static Path createSymbolicLink(Path link, Path target, FileAttribute<?>... attrs) throws IOException {
        return link;
    }

    public static Path readSymbolicLink(Path link) throws IOException {
        return link;
    }

    public static java.nio.file.attribute.FileTime getLastModifiedTime(Path path, LinkOption... options) throws IOException {
        return null;
    }

    public static Path setLastModifiedTime(Path path, java.nio.file.attribute.FileTime time) throws IOException {
        return path;
    }

    public static Object getOwner(Path path, LinkOption... options) throws IOException {
        return null;
    }

    public static Path setOwner(Path path, java.nio.file.attribute.UserPrincipal owner) throws IOException {
        return path;
    }

    public static java.util.Set<java.nio.file.attribute.PosixFilePermission> getPosixFilePermissions(Path path, LinkOption... options) throws IOException {
        return new java.util.HashSet<>();
    }

    public static Path setPosixFilePermissions(Path path, java.util.Set<java.nio.file.attribute.PosixFilePermission> perms) throws IOException {
        return path;
    }
}
