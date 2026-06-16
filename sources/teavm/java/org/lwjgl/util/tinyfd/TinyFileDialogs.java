package org.lwjgl.util.tinyfd;

public final class TinyFileDialogs {
    public static String tinyfd_inputBox(String title, String message, String defaultInput) { return ""; }
    public static String tinyfd_saveFileDialog(String title, String defaultPathAndFile, String[] filterPatterns, String filterDescription) { return ""; }
    public static String tinyfd_openFileDialog(String title, String defaultPathAndFile, String[] filterPatterns, String filterDescription, boolean allowMultipleSelects) { return ""; }
    public static int tinyfd_messageBox(String title, String message, String type, String icon, int defaultButton) { return 1; }
    public static int tinyfd_notifyPopup(String title, String message, String icon) { return 1; }
    public static String tinyfd_colorChooser(String title, String defaultHexRGB, byte[] defaultRGB, byte[] resultRGB) { return "#000000"; }
    public static void openFolderDialog(String title) {}
    public static String getDefaultPath() { return ""; }

    public static int tinyfd_messageBox(java.lang.CharSequence title, java.lang.CharSequence message, java.lang.CharSequence type, java.lang.CharSequence icon, int defaultButton) { return 1; }
}
