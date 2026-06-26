package net.minecraft.util.datafix;

// PATCHED: DataFixers returns null (handled by __safe stub at runtime).
// We can't extend DataFixer (it's FINAL in the jar and jar patching fails).
// The __safe stub + Proxy-on-prototype handles all method calls on null.

public class DataFixers {
    public static com.mojang.datafixers.DataFixer getDataFixer() {
        return null;
    }
    
    public static Object getFileFixer() {
        return null;
    }
}
