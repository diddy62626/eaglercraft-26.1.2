package net.minecraft.util.worldupdate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.network.chat.Component;
public class UpgradeProgress {
    public static class FileFixStats {
        public int finishedOperations() { return 0; }
        public int totalOperations() { return 0; }
        public void reset() {}
        public float getProgress() { return 0f; }
    }
    public static class Status {}
    public static class Type {
        public Component label() { return null; }
    }
    public FileFixStats getTotalFileFixStats() { return null; }
    public FileFixStats getTypeFileFixStats() { return null; }
    public FileFixStats getRunningFileFixerStats() { return null; }
    public boolean isFinished() { return false; }
    public void setFinished(boolean finished) {}
    public void addTotalFileFixOperations(int ops) {}
    public float getTotalProgress() { return 0f; }
    public void setTotalProgress(float progress) {}
    public int getTotalChunks() { return 0; }
    public void addTotalChunks(int chunks) {}
    public int getConverted() { return 0; }
    public void incrementConverted() {}
    public int getSkipped() { return 0; }
    public void incrementSkipped() {}
    public void incrementFinishedOperations() {}
    public void incrementFinishedOperationsBy(int n) {}
    public void setCanceled() {}
    public boolean isCanceled() { return false; }
    public void setDimensionProgress(ResourceKey<?> key, float progress) {}
    public float getDimensionProgress(ResourceKey<?> key) { return 0f; }
    public Status getStatus() { return null; }
    public void setStatus(Status status) {}
    public DataFixTypes getDataFixType() { return null; }
    public void setType(Type type) {}
    public Type getType() { return null; }
    public void setApplicableFixerAmount(int n) {}
    public void incrementRunningFileFixer() {}
    public void reset(DataFixTypes type) {}
    public void logProgress() {}
}
