package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.Set;

/**
 * EaglerCraft stub for net.minecraft.server.packs.PackResources.
 */
public interface PackResources {
    InputStream getResource(String location);
    Set<String> getNamespaces();
    void close();
}
