package net.minecraft.server.packs.metadata;

import com.google.gson.JsonObject;

/**
 * EaglerCraft stub for MC 26.1.2 MetadataSectionType interface.
 */
public interface MetadataSectionType<T> {
    T fromJson(JsonObject json);
    JsonObject toJson(T value);
    String getMetadataSectionName();
}
