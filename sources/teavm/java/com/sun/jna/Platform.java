package com.sun.jna;

public final class Platform {
    public static final int UNSPECIFIED = -1;
    public static final int MAC = 0;
    public static final int LINUX = 1;
    public static final int WINDOWS = 3;
    public static final int SOLARIS = 2;
    public static final int FREEBSD = 4;
    public static final int OPENBSD = 5;
    public static final int WINDOWSCE = 6;
    public static int getOSType() { return LINUX; }
    public static boolean isMac() { return false; }
    public static boolean isLinux() { return false; }
    public static boolean isWindows() { return false; }
    public static boolean isSolaris() { return false; }
    public static boolean isFreeBSD() { return false; }
    public static boolean isOpenBSD() { return false; }
    public static boolean isWindowsCE() { return false; }
    public static boolean isX11() { return false; }
    public static boolean hasRuntimeExec() { return true; }
    public static boolean is64Bit() { return true; }
    public static boolean isIntel() { return true; }
    public static boolean isPPC() { return false; }
    public static boolean isARM() { return false; }
    public static String getNativeArchitecture() { return "generic"; }
    public static String getOSPrefix() { return "linux"; }
    public static String getOSName() { return "linux"; }
    public static String getOSVersion() { return "0.0"; }
}
