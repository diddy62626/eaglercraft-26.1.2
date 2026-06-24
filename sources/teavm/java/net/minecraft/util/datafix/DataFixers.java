package net.minecraft.util.datafix;

// PATCHED: DataFixers for browser environment.
// The original clinit builds a massive DataFixer using DSL methods
// that return null in TeaVM (stub methods). This causes the MC
// constructor to freeze/crash.
//
// This patch provides a no-op DataFixer that lets MC construct
// without the real data fixer. World loading may not work perfectly
// but the game won't freeze.

public class DataFixers {
    private static com.mojang.datafixers.DataFixer DATA_FIXER;
    
    public static com.mojang.datafixers.DataFixer getDataFixer() {
        if (DATA_FIXER == null) {
            DATA_FIXER = new com.mojang.datafixers.DataFixer() {
                @Override
                public com.mojang.datafixers.Typed<java.util.Optional<java.util.Map<java.lang.String, ?>>> fix(com.mojang.datafixers.Typed<?> typed, com.mojang.datafixers.DSL.TypeReference type) {
                    return typed.cast(type);
                }
                @Override
                public com.mojang.datafixers.Typed<?> fixUp(com.mojang.datafixers.Typed<?> typed, com.mojang.datafixers.DSL.TypeReference oldType, com.mojang.datafixers.DSL.TypeReference newType) {
                    return typed;
                }
                @Override
                public com.mojang.datafixers.Typed<?> fixUp(com.mojang.datafixers.Typed<?> typed, com.mojang.datafixers.DSL.TypeReference oldType, int newVersion) {
                    return typed;
                }
                @Override
                public int getSchema(int version) {
                    return 0;
                }
                @Override
                public com.mojang.datafixers.schemas.Schema getSchema(int version, com.mojang.datafixers.DSL.TypeReference type) {
                    return null;
                }
                @Override
                public com.mojang.datafixers.Typed<?> fixUpRaw(com.mojang.datafixers.Typed<?> typed, com.mojang.datafixers.DSL.TypeReference oldType, int newVersion) {
                    return typed;
                }
            };
        }
        return DATA_FIXER;
    }
    
    public static net.minecraft.util.filefix.FileFixerUpper getFileFixer() {
        return null;
    }
}
