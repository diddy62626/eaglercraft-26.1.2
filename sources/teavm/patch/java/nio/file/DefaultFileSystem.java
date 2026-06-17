package java.nio.file;
class DefaultFileSystem extends FileSystem {
    public Iterable<Path> getRootDirectories() { return java.util.Collections.emptyList(); }
    public PathMatcher getPathMatcher(String syntaxAndPattern) { return path -> false; }
    public Iterable<FileStore> getFileStores() { return java.util.Collections.emptyList(); }
    public java.util.Set<String> supportedFileAttributeViews() { return java.util.Collections.emptySet(); }
}