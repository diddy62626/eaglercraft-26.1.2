package com.mojang.authlib.minecraft;

import com.mojang.authlib.SignatureState;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

public final class MinecraftProfileTextures {
    public static final MinecraftProfileTextures EMPTY = new MinecraftProfileTextures(null, null, null, SignatureState.UNSIGNED);

    private final MinecraftProfileTexture skin;
    private final MinecraftProfileTexture cape;
    private final MinecraftProfileTexture elytra;
    private final SignatureState signatureState;

    public MinecraftProfileTextures(MinecraftProfileTexture skin, MinecraftProfileTexture cape,
                                     MinecraftProfileTexture elytra, SignatureState signatureState) {
        this.skin = skin;
        this.cape = cape;
        this.elytra = elytra;
        this.signatureState = signatureState;
    }

    public MinecraftProfileTexture get(MinecraftProfileTexture.Type type) {
        switch (type) {
            case SKIN: return skin;
            case CAPE: return cape;
            case ELYTRA: return elytra;
            default: return null;
        }
    }

    public SignatureState signatureState() { return signatureState; }

    public MinecraftProfileTexture skin() { return skin; }
    public MinecraftProfileTexture cape() { return cape; }
    public MinecraftProfileTexture elytra() { return elytra; }
}
