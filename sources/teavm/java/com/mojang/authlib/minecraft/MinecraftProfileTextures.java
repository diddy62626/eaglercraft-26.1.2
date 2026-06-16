package com.mojang.authlib.minecraft;

import com.mojang.authlib.properties.Property;

public final class MinecraftProfileTextures {
    private final Property source;

    public MinecraftProfileTextures(Property source) {
        this.source = source;
    }

    public Property get(Property property) { return source; }
    public com.mojang.authlib.minecraft.MinecraftProfileTexture get(com.mojang.authlib.minecraft.MinecraftProfileTexture.Type type) {
        return null;
    }

    public enum Type {
        SKIN, CAPE, ELYTRA;
    }
}
