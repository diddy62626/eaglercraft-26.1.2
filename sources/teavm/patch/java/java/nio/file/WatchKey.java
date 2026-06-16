package java.nio.file;
public interface WatchKey {
    boolean isValid();
    java.util.List<WatchEvent<?>> pollEvents();
    boolean reset();
    void cancel();
    Watchable watchable();
}