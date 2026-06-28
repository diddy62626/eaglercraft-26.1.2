package net.lax1dude.eaglercraft.v2_6.patch;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.OpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.spi.FileSystemProvider;

/**
 * Stub JarFileSystemProvider for the "jar" URI scheme.
 *
 * Minecraft's Util.<clinit> calls FileSystemProvider.installedProviders()
 * and looks for a provider with scheme "jar". If none is found, it throws
 * IllegalStateException("No jar file system provider found"), which crashes
 * the Util class static initializer.
 *
 * In EaglerCraft, resources come from the EPK bundle, not JAR files.
 * This provider exists solely to satisfy the Util.<clinit> lookup.
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
