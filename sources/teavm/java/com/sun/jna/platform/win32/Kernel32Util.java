package com.sun.jna.platform.win32;

public final class Kernel32Util {
    public static String getLastError() { return ""; }
    public static String formatMessage(int errorCode) { return ""; }
    public static String formatMessageFromLastErrorCode(int errorCode) { return ""; }
    public static int getLastErrorValue() { return 0; }
    public static String[] getEnvironmentStrings() { return new String[0]; }
    public static boolean isWindows() { return false; }
    public static boolean isWindows7OrLater() { return false; }
    public static boolean isWindows10OrLater() { return false; }
    public static boolean isWindowsVistaOrLater() { return false; }
    public static boolean isWindowsXPOrLater() { return false; }

    public static java.util.List<String> getModules(int processId) { return new java.util.ArrayList<>(); }
}
