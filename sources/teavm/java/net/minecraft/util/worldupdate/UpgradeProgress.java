package net.minecraft.util.worldupdate;
public class UpgradeProgress {
    public static class FileFixStats {
        public int totalOperations() { return 0; }
        public float getProgress() { return 0f; }
    }
    public FileFixStats getTotalFileFixStats() { return null; }
    public FileFixStats getTypeFileFixStats() { return null; }
    public FileFixStats getRunningFileFixerStats() { return null; }
}
