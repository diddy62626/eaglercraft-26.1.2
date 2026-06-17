package java.nio.file;
public interface WatchService extends java.io.Closeable {
    WatchKey poll();
    WatchKey poll(long timeout, java.util.concurrent.TimeUnit unit);
    WatchKey take();
}