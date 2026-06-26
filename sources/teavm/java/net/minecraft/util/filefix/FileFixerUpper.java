package net.minecraft.util.filefix;

// Use raw types to avoid javac crash with DFU generics
public class FileFixerUpper {
    public static boolean requiresFileFixing(int version) {
        return false;
    }
    public static Object fix(Object access, Object dynamic, Object progress) {
        return dynamic;
    }
}
