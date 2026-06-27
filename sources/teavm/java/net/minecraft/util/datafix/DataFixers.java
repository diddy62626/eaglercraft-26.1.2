package net.minecraft.util.datafix;

import com.mojang.datafixers.schemas.Schema;

public class DataFixers {
    public static class NoOpDataFixer implements com.mojang.datafixers.DataFixer {
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
    public static net.minecraft.util.filefix.FileFixerUpper getFileFixer() {
        return null;
    }
}
