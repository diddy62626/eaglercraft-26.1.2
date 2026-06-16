package net.minecraft.server.packs.resources;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface IoSupplier<T> {
    T get() throws IOException;
}
