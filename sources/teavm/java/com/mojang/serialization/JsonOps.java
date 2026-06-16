package com.mojang.serialization;

import java.util.Map;
import java.util.stream.Stream;

public final class JsonOps implements DynamicOps<com.google.gson.JsonElement> {
    public static final JsonOps INSTANCE = new JsonOps();
    public static final JsonOps COMPRESSED = new JsonOps();

    private JsonOps() {}

    @Override public com.google.gson.JsonElement empty() { return new com.google.gson.JsonNull(); }
    @Override public com.google.gson.JsonElement emptyList() { return new com.google.gson.JsonArray(); }
    @Override public com.google.gson.JsonElement emptyMap() { return new com.google.gson.JsonObject(); }

    @Override public com.google.gson.JsonElement createBoolean(boolean value) { return new com.google.gson.JsonPrimitive(value); }
    @Override public com.google.gson.JsonElement createInt(int value) { return new com.google.gson.JsonPrimitive(value); }
    @Override public com.google.gson.JsonElement createLong(long value) { return new com.google.gson.JsonPrimitive(value); }
    @Override public com.google.gson.JsonElement createFloat(float value) { return new com.google.gson.JsonPrimitive(value); }
    @Override public com.google.gson.JsonElement createDouble(double value) { return new com.google.gson.JsonPrimitive(value); }
    @Override public com.google.gson.JsonElement createString(String value) { return new com.google.gson.JsonPrimitive(value); }

    public com.google.gson.JsonElement convertList(DynamicOps<com.google.gson.JsonElement> outOps, com.google.gson.JsonElement input) { return input; }
    public com.google.gson.JsonElement convertMap(DynamicOps<com.google.gson.JsonElement> outOps, com.google.gson.JsonElement input) { return input; }

    public Object convertTo(DynamicOps<?> outOps, com.google.gson.JsonElement input) { return input; }
}
