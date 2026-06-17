package net.minecraft.server.packs.metadata;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;

public class MetadataSectionType<T> {
    private final String name;
    private final Codec<T> codec;

    public MetadataSectionType(String name, Codec<T> codec) {
        this.name = name;
        this.codec = codec;
    }

    public String name() { return name; }
    public Codec<T> codec() { return codec; }

    public T fromJson(JsonObject json) { return null; }
    public JsonObject toJson(T value) { return new JsonObject(); }
    public String getMetadataSectionName() { return name; }
}
