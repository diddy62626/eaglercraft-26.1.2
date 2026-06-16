package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.Set;

/**
 * EaglerCraft stub for net.minecraft.server.packs.PackResources.
 *
 * MC 26.1.2 API:
 *   packId() returns String
 *   getNamespaces(PackType) returns Set<String>
 *   getMetadataSection(MetadataSectionType) returns Object
 */
public interface PackResources {
    InputStream getResource(String location);
    Set<String> getNamespaces();
    void close();

    /**
     * MC 26.1.2: Returns the unique pack identifier.
     */
    String packId();

    /**
     * MC 26.1.2: Returns the set of namespaces this pack provides.
     */
    Set<String> getNamespaces(PackType packType);

    /**
     * MC 26.1.2: Returns a metadata section by type.
     */
    <T> T getMetadataSection(net.minecraft.server.packs.metadata.MetadataSectionType<T> type);

    default java.util.function.Function<net.minecraft.server.packs.resources.IoSupplier<java.io.InputStream>, java.io.InputStream> createResourceOpener() { return null; }
    default net.minecraft.server.packs.resources.IoSupplier<java.io.InputStream> getResource(net.minecraft.server.packs.PackType packType, net.minecraft.resources.Identifier location) { return null; }
    default net.minecraft.server.packs.resources.IoSupplier<java.io.InputStream> getRootResource(String... path) { return null; }
    default void listResources(net.minecraft.server.packs.PackType packType, String namespace, String pathPrefix, ResourceOutput output) {}

    @FunctionalInterface
    interface ResourceOutput {
        void accept(net.minecraft.resources.Identifier location, net.minecraft.server.packs.resources.IoSupplier<java.io.InputStream> supplier);
        default void accept(Object location, Object supplier) {}
    }
}
