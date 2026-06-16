package com.mojang.authlib;

import java.util.UUID;

/**
 * EaglerCraft 26.1.2 browser stub for com.mojang.authlib.GameProfile.
 * Minimal game profile used by the User class.
 */
public class GameProfile {

	private final UUID id;
	private final String name;

	public GameProfile(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isComplete() {
		return id != null && name != null && !name.isEmpty();
	}

	@Override
	public String toString() {
		return "GameProfile{id=" + id + ", name=" + name + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof GameProfile)) return false;
		GameProfile other = (GameProfile) obj;
		if (id != null ? !id.equals(other.id) : other.id != null) return false;
		return name != null ? name.equals(other.name) : other.name == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}
}
