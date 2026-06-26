package net.minecraft.util.datafix;

import com.mojang.datafixers.schemas.Schema;

// PATCHED: DataFixers returns a no-op DataFixer.
// The DFU jar's DataFixer class is FINAL, but we patched the jar
// to remove the FINAL flag (scripts/patch_dfu_final.py).

public class DataFixers {
    
    public static class NoOpDataFixer extends com.mojang.datafixers.DataFixer {
        @Override
        public com.mojang.serialization.Dynamic update(
                com.mojang.datafixers.DSL.TypeReference type,
                com.mojang.serialization.Dynamic dynamic,
                int version, int newVersion) {
            return dynamic;
        }
        
        @Override
        public Schema getSchema(int version) {
            return new Schema(version, null);
        }
    }
    
    private static com.mojang.datafixers.DataFixer DATA_FIXER = new NoOpDataFixer();
    
    public static com.mojang.datafixers.DataFixer getDataFixer() {
        return DATA_FIXER;
    }
    
    public static Object getFileFixer() {
        return null;
    }
}
