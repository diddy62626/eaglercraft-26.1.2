package net.minecraft.server.packs.resources;

import java.nio.file.Path;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

@FunctionalInterface
public interface IoSupplier<T> {
    T get() throws java.io.IOException;

    static IoSupplier<java.io.InputStream> create(Path path) {
        return () -> java.nio.file.Files.newInputStream(path);
    }
    static IoSupplier<java.io.InputStream> create(ZipFile zipFile, ZipEntry entry) {
        return () -> zipFile.getInputStream(entry);
    }
}
