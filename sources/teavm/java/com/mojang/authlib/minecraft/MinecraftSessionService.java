package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.ProfileResult;

/**
 * EaglerCraft 26.1.2 browser stub for com.mojang.authlib.minecraft.MinecraftSessionService.
 * No real session service in the browser - authentication is handled by the gateway.
 *
 * NOTE: MC 26.1.2 changed fetchProfile(UUID, boolean) to return
 * ProfileResult (not GameProfile). We mirror that signature here.
 */
public interface MinecraftSessionService {

        /**
         * Joins a multiplayer server session. No-op in EaglerCraft.
         */
        void joinServer(GameProfile profile, String authenticationToken, String serverId);

        /**
         * Fetches a game profile from the session service by name.
         * Returns null in EaglerCraft.
         */
        GameProfile fetchProfile(String name, boolean secure);

        /**
         * MC 26.1.2: Fetches a profile result by UUID.
         * Returns null in EaglerCraft (no real session service in browser).
         */
        ProfileResult fetchProfile(java.util.UUID uuid, boolean secure);

        /**
         * MC 26.1.2: Returns the packed textures Property for the given profile.
         * Browser: returns null (no real session service).
         */
        default Property getPackedTextures(GameProfile profile) {
                return null;
        }

        /**
         * MC 26.1.2: Unpacks the textures Property into a MinecraftProfileTextures object.
         * Browser: returns an empty MinecraftProfileTextures.
         */
        default MinecraftProfileTextures unpackTextures(Property property) {
                return new MinecraftProfileTextures(null, null, null, com.mojang.authlib.SignatureState.UNSIGNED);
        }
}
