package net.minecraft.util.datafix;

// PATCHED: DataFixers returns a no-op fixer.
// Uses Object return type to avoid needing DataFixer interface.
// The MissingMethodTransformer handles missing methods at runtime.

public class DataFixers {
    private static Object DATA_FIXER = new Object();
    
    public static Object getDataFixer() {
        return DATA_FIXER;
    }
}
