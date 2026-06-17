package com.mojang.authlib.minecraft;

import java.net.URL;

public final class MinecraftProfileTexture {
    private final String url;
    private final String hash;

    public MinecraftProfileTexture(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() { return url; }
    public String getHash() { return hash; }
    public URL getUrlObject() { return null; }

    public enum Type {
        SKIN, CAPE, ELYTRA;
    }
}
