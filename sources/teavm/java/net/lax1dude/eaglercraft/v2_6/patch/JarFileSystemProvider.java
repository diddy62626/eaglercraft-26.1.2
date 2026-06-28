package net.lax1dude.eaglercraft.v2_6.patch;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;

/**
 * Stub JarFileSystemProvider for the "jar" URI scheme.
 *
 * Minecraft's Util.<clinit> calls FileSystemProvider.installedProviders()
 * and looks for a provider with scheme "jar". If none is found, it throws
 * IllegalStateException("No jar file system provider found"), which crashes
 * the Util class static initializer. Since Util is used everywhere in MC,
 * this cascades into NullPointerExceptions throughout the game.
 *
 * In EaglerCraft, resources come from the EPK bundle, not JAR files.
 * This provider exists solely to satisfy the Util.<clinit> lookup.
 * All methods return null/false/0/no-op since jar: URIs are never used
 * at runtime in the browser.
 */
public class JarFileSystemProvider extends FileSystemProvider {
    @Override
    public String getScheme() { return "jar"; }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String,?> env) { return null; }

    @Override
    public FileSystem newFileSystem(Path path, Map<String,?> env) { return null; }

    @Override
    public FileSystem getFileSystem(URI uri) { return null; }

    @Override
    public Path getPath(URI uri) { return null; }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) { return null; }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) { return null; }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) {}

    @Override
    public void delete(Path path) {}

    @Override
    public void copy(Path source, Path target, CopyOption... options) {}

    @Override
    public void move(Path source, Path target, CopyOption... options) {}

    @Override
    public boolean isSameFile(Path path, Path path2) { return false; }

    @Override
    public boolean isHidden(Path path) { return false; }

    @Override
    public FileStore getFileStore(Path path) { return null; }

    @Override
    public void checkAccess(Path path, AccessMode... modes) {}

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) { return null; }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) { return null; }

    @Override
    public Map<String,Object> readAttributes(Path path, String attributes, LinkOption... options) { return null; }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) {}
}
