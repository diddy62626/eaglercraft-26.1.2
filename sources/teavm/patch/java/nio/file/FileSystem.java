package java.nio.file;
import java.util.Set;
public abstract class FileSystem {
    public abstract Iterable<Path> getRootDirectories();
    public abstract PathMatcher getPathMatcher(String syntaxAndPattern);
    public abstract Iterable<FileStore> getFileStores();
    public abstract Set<String> supportedFileAttributeViews();
}