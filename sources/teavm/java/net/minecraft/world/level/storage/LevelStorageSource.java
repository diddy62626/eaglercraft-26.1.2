package net.minecraft.world.level.storage;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.repository.PackRepository;
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
    public LevelStorageAccess validateAndCreateAccess(String name) {
        return new LevelStorageAccess();
    }
    public LevelCandidates findLevelCandidates() {
        return new LevelCandidates();
    }
    public boolean levelExists(String name) {
        return false;
    }
    public CompletableFuture<Object> loadLevelSummaries(LevelCandidates candidates) {
        return CompletableFuture.completedFuture(null);
    }
    public static WorldLoader.PackConfig getPackConfig(Dynamic<?> dynamic, PackRepository repo, boolean flag) {
        return null;
    }
    public DirectoryValidator getWorldDirValidator() { return validator; }
    public class LevelStorageAccess {
        public void close() {}
        public void safeClose() {}
        public LevelStorageSource parent() { return LevelStorageSource.this; }
        public boolean hasWorldData() { return false; }
        public String getLevelId() { return ""; }
        public Dynamic<?> getUnfixedDataTag(boolean flag) { return null; }
        public void collectIssues(boolean flag) {}
        public LevelSummary fixAndGetSummaryFromTag(Dynamic<?> tag) { return null; }
        public Instant getFileModificationTime(boolean flag) { return Instant.EPOCH; }
        public Path getLevelPath(LevelResource resource) { return null; }
    }
    public static class LevelCandidates {}
}
