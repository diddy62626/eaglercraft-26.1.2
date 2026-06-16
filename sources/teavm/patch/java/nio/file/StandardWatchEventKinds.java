package java.nio.file;
public class StandardWatchEventKinds {
    public static final WatchEvent.Kind<Path> ENTRY_CREATE = new WatchEvent.Kind<Path>() { public String name() { return "ENTRY_CREATE"; } public Class<Path> type() { return Path.class; } };
    public static final WatchEvent.Kind<Path> ENTRY_DELETE = new WatchEvent.Kind<Path>() { public String name() { return "ENTRY_DELETE"; } public Class<Path> type() { return Path.class; } };
    public static final WatchEvent.Kind<Path> ENTRY_MODIFY = new WatchEvent.Kind<Path>() { public String name() { return "ENTRY_MODIFY"; } public Class<Path> type() { return Path.class; } };
    public static final WatchEvent.Kind<Object> OVERFLOW = new WatchEvent.Kind<Object>() { public String name() { return "OVERFLOW"; } public Class<Object> type() { return Object.class; } };
}