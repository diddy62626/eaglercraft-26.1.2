package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.GameProfile;

import java.util.Collections;
import java.util.Set;

/**
 * EaglerCraft stub for MC 26.1.2 com.mojang.authlib.yggdrasil.ProfileResult.
 *
 * Wraps a GameProfile with the set of actions performed by the session
 * service's fetchProfile(UUID, boolean) call. Browser: no real auth,
 * so ProfileResult is just a wrapper around the profile with an empty
 * action set.
 *
 * MC 26.1.2 constructor signature: ProfileResult(GameProfile profile, Set actions)
 * actions() returns the Set.
 */
public final class ProfileResult {
    private final GameProfile profile;
    private final Set<Object> actions;

    @SuppressWarnings("unchecked")
    public ProfileResult(GameProfile profile, Set<?> actions) {
        this.profile = profile;
        this.actions = actions == null ? Collections.emptySet() : (Set<Object>) actions;
    }

    public ProfileResult(GameProfile profile) {
        this(profile, Collections.emptySet());
    }

    public GameProfile profile() {
        return profile;
    }

    public Set<?> actions() {
        return actions;
    }
}
