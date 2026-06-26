package net.minecraft.util.datafix;

// Return Object to avoid referencing DataFixer (triggers javac NPE)
public class DataFixers {
    public static Object getDataFixer() {
        return null;
    }
    public static Object getFileFixer() {
        return null;
    }
}
