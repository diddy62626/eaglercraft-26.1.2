package org.lwjgl.openal;

public final class ALUtil {
    public static boolean alcIsSoftExtensionPresent(long device, String extension) { return false; }
    public static boolean alIsSoftExtensionPresent(String extension) { return false; }

    public static java.util.List<String> getStringList(long device, int param) {
        return new java.util.ArrayList<>();
    }
}
