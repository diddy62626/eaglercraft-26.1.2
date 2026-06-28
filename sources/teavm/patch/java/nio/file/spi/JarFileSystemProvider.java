package java.nio.file.spi;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;

/**
 * Stub JarFileSystemProvider for the "jar" URI scheme.
 *
 * Minecraft's Util.<clinit> calls FileSystemProvider.installedProviders()
 * and looks for a provider with scheme "jar". If none is found, it throws
 * IllegalStateException("No jar file system provider found"), which crashes
 * the Util class static initializer. Since Util is used everywhere in MC,
 * this cascades into NullPointerExceptions throughout the game.
 *
 * In a browser/EaglerCraft environment, there are no real JAR files —
 * resources are loaded from the EPK asset bundle. This provider exists
 * solely to satisfy the Util.<clinit> lookup and prevent the crash.
 * All methods return null/empty/no-op since jar: URIs are never used
 * at runtime in the browser.
 */
public class JarFileSystemProvider extends FileSystemProvider {
    @Override
    public String getScheme() {
        return "jar";
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String,?> env) {
        return null;
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        return null;
    }

    @Override
    public Path getPath(URI uri) {
        return null;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) {
        return null;
    }
}
