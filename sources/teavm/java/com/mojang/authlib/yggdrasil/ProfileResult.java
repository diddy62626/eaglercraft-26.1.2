package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.GameProfile;

/**
 * EaglerCraft stub for MC 26.1.2 com.mojang.authlib.yggdrasil.ProfileResult.
 *
 * Wraps a GameProfile with signatory/property metadata returned by the
 * session service's fetchProfile(UUID, boolean) call. Browser: no real
 * auth, so ProfileResult is just a wrapper around the profile.
 */
public final class ProfileResult {
    private final GameProfile profile;
    final boolean joinedAt; // marker to differentiate from single-arg constructor

    public ProfileResult(GameProfile profile, boolean joinedAt) {
        this.profile = profile;
        this.joinedAt = joinedAt;
    }

    public ProfileResult(GameProfile profile) {
        this(profile, false);
    }

    public GameProfile profile() {
        return profile;
    }
}
