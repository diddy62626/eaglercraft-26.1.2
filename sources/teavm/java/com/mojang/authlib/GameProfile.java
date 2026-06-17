package com.mojang.authlib;

import java.util.UUID;
import com.mojang.authlib.properties.PropertyMap;

/**
 * EaglerCraft stub for com.mojang.authlib.GameProfile.
 * MC 26.1.2 uses a record with (UUID id, String name, PropertyMap properties).
 * We provide both record-style accessors (id(), name(), properties()) and
 * JavaBean-style accessors (getId(), getName()) for backwards compatibility.
 */
public class GameProfile {
    private final UUID id;
    private final String name;
    private final PropertyMap properties;

    public GameProfile(UUID id, String name, PropertyMap properties) {
        this.id = id;
        this.name = name;
        this.properties = properties != null ? properties : new PropertyMap();
    }

    public GameProfile(UUID id, String name) {
        this(id, name, new PropertyMap());
    }

    // Record-style accessors (used by MC 26.1.2)
    public UUID id() { return id; }
    public String name() { return name; }
    public PropertyMap properties() { return properties; }

    // JavaBean-style accessors (used by older code)
    public UUID getId() { return id; }
    public String getName() { return name; }
    public PropertyMap getProperties() { return properties; }

    public boolean isComplete() {
        return id != null && name != null && !name.isEmpty();
    }

    @Override
    public String toString() {
        return "GameProfile{id=" + id + ", name=" + name + ", properties=" + properties + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameProfile)) return false;
        GameProfile other = (GameProfile) obj;
        if (id != null ? !id.equals(other.id) : other.id != null) return false;
        if (name != null ? !name.equals(other.name) : other.name != null) return false;
        return properties != null ? properties.equals(other.properties) : other.properties == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
