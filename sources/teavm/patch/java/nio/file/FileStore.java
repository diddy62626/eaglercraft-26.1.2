package java.nio.file;
public abstract class FileStore {
    public abstract String name();
    public abstract String type();
    public abstract boolean isReadOnly();
    public abstract long getTotalSpace();
    public abstract long getUsableSpace();
    public abstract long getUnallocatedSpace();
}