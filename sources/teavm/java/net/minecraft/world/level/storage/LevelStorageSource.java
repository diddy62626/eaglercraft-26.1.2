package net.minecraft.world.level.storage;
import java.nio.file.Path;
import com.mojang.datafixers.DataFixer;
import net.minecraft.world.level.validation.DirectoryValidator;
public class LevelStorageSource {
    private final Path baseDir;
    private final Path backupDir;
    private final DirectoryValidator validator;
    private final DataFixer fixerUpper;
    public LevelStorageSource(Path baseDir, Path backupDir, DirectoryValidator validator, DataFixer fixerUpper) {
        this.baseDir = baseDir;
        this.backupDir = backupDir;
        this.validator = validator;
        this.fixerUpper = fixerUpper;
    }
    public static DirectoryValidator parseValidator(Path path) {
        return null;
    }
    public LevelStorageAccess createAccess(String name) {
        return new LevelStorageAccess();
    }
    public LevelCandidates findLevelCandidates() {
        return new LevelCandidates();
    }
    public boolean levelExists(String name) {
        return false;
    }
    public static class LevelStorageAccess {
        public void close() {}
        public boolean hasWorldData() { return false; }
    }
    public static class LevelCandidates {}
}
