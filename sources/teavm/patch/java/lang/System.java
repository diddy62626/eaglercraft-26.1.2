package java.lang;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * TeaVM stub for java.lang.System.
 * Browser-compatible replacements for native methods.
 * These stubs fill in the gaps that TeaVM's classlib doesn't cover.
 * TeaVM will provide the real implementations for methods it supports internally.
 */
public final class System {

    private System() {}

    public static InputStream in = null;
    public static PrintStream out = null;
    public static PrintStream err = null;

    public static void setIn(InputStream s) { in = s; }
    public static void setOut(PrintStream s) { out = s; }
    public static void setErr(PrintStream s) { err = s; }

    public static long currentTimeMillis() {
        // TeaVM classlib provides the real implementation
        // This is a fallback for when the patch is compiled with javac
        return 0L;
    }

    public static long nanoTime() {
        return currentTimeMillis() * 1_000_000L;
    }

    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        if (src == null || dest == null) throw new NullPointerException();
        if (srcPos < 0 || destPos < 0 || length < 0) throw new IndexOutOfBoundsException();
        if (src instanceof Object[] && dest instanceof Object[]) {
            Object[] sa = (Object[]) src;
            Object[] da = (Object[]) dest;
            if (srcPos < destPos) {
                for (int i = length - 1; i >= 0; i--) da[destPos + i] = sa[srcPos + i];
            } else {
                for (int i = 0; i < length; i++) da[destPos + i] = sa[srcPos + i];
            }
        } else if (src instanceof byte[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof int[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof long[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof float[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof double[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof char[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof short[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        } else if (src instanceof boolean[]) {
            System.arraycopyPrim(src, srcPos, dest, destPos, length);
        }
    }

    private static void arraycopyPrim(Object src, int srcPos, Object dest, int destPos, int length) {
        if (src instanceof byte[]) {
            byte[] sa = (byte[]) src; byte[] da = (byte[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof int[]) {
            int[] sa = (int[]) src; int[] da = (int[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof long[]) {
            long[] sa = (long[]) src; long[] da = (long[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof float[]) {
            float[] sa = (float[]) src; float[] da = (float[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof double[]) {
            double[] sa = (double[]) src; double[] da = (double[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof char[]) {
            char[] sa = (char[]) src; char[] da = (char[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof short[]) {
            short[] sa = (short[]) src; short[] da = (short[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        } else if (src instanceof boolean[]) {
            boolean[] sa = (boolean[]) src; boolean[] da = (boolean[]) dest;
            if (sa == da && srcPos < destPos) { for (int i = length-1; i >= 0; i--) da[destPos+i] = sa[srcPos+i]; }
            else { for (int i = 0; i < length; i++) da[destPos+i] = sa[srcPos+i]; }
        }
    }

    public static int identityHashCode(Object x) {
        return x == null ? 0 : x.hashCode();
    }

    public static String getProperty(String key) {
        if (key == null) throw new NullPointerException();
        switch (key) {
            case "java.version": return "25";
            case "java.vm.version": return "25";
            case "java.runtime.version": return "25";
            case "java.specification.version": return "25";
            case "java.vendor": return "EaglerCraft";
            case "java.vendor.url": return "";
            case "java.vendor.version": return "26.1.2";
            case "java.home": return "/eaglercraft";
            case "java.class.path": return ".";
            case "java.library.path": return "/eaglercraft";
            case "java.io.tmpdir": return "/eaglercraft/tmp";
            case "java.ext.dirs": return "";
            case "os.name": return "Browser";
            case "os.arch": return "wasm";
            case "os.version": return "1.0";
            case "file.separator": return "/";
            case "path.separator": return ":";
            case "line.separator": return "\n";
            case "user.name": return "Player";
            case "user.home": return "/eaglercraft";
            case "user.dir": return "/eaglercraft";
            case "user.country": return "US";
            case "user.language": return "en";
            case "minecraft.version": return "26.1.2";
            default: return null;
        }
    }

    public static String getProperty(String key, String def) {
        String val = getProperty(key);
        return val != null ? val : def;
    }

    public static String setProperty(String key, String value) { return null; }
    public static String clearProperty(String key) { return null; }

    public static java.util.Properties getProperties() {
        java.util.Properties p = new java.util.Properties();
        p.setProperty("java.version", "25");
        p.setProperty("os.name", "Browser");
        p.setProperty("os.arch", "wasm");
        p.setProperty("user.name", "Player");
        p.setProperty("user.home", "/eaglercraft");
        p.setProperty("user.dir", "/eaglercraft");
        p.setProperty("file.separator", "/");
        p.setProperty("line.separator", "\n");
        return p;
    }

    public static void setProperties(java.util.Properties props) {}

    public static String getenv(String name) { return null; }

    public static java.util.Map<String,String> getenv() {
        return java.util.Collections.emptyMap();
    }

    public static void exit(int status) {}

    public static void gc() {}

    public static void load(String filename) {
        throw new UnsatisfiedLinkError("Cannot load native library in browser: " + filename);
    }

    public static void loadLibrary(String libname) {
        throw new UnsatisfiedLinkError("Cannot load native library in browser: " + libname);
    }

    public static String mapLibraryName(String libname) { return libname; }

    public static void setSecurityManager(SecurityManager s) {}
    public static SecurityManager getSecurityManager() { return null; }
    public static java.nio.channels.Channel inheritedChannel() throws java.io.IOException { return null; }

}
