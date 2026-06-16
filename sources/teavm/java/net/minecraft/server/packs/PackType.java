package net.minecraft.server.packs;

/**
 * EaglerCraft stub for MC 26.1.2 PackType enum.
 */
public enum PackType {
    CLIENT_RESOURCES,
    SERVER_DATA;

    public String getDirectory() {
        switch (this) {
            case CLIENT_RESOURCES: return "assets";
            case SERVER_DATA: return "data";
            default: return "assets";
        }
    }
}
