package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;

/**
 * EaglerCraft 26.1.2 browser stub for com.mojang.authlib.minecraft.MinecraftSessionService.
 * No real session service in the browser - authentication is handled by the gateway.
 */
public interface MinecraftSessionService {

	/**
	 * Joins a multiplayer server session. No-op in EaglerCraft.
	 */
	void joinServer(GameProfile profile, String authenticationToken, String serverId);

	/**
	 * Fetches a game profile from the session service. Returns null in EaglerCraft.
	 */
	GameProfile fetchProfile(String name, boolean secure);

	/**
	 * Fetches a game profile by UUID. Returns null in EaglerCraft.
	 */
	GameProfile fetchProfile(java.util.UUID uuid, boolean secure);
}
