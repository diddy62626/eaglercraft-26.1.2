package java.nio.file.attribute;

public interface BasicFileAttributes {
    java.nio.file.attribute.FileTime lastModifiedTime();
    java.nio.file.attribute.FileTime creationTime();
    java.nio.file.attribute.FileTime lastAccessTime();
    boolean isRegularFile();
    boolean isDirectory();
    boolean isSymbolicLink();
    boolean isOther();
    long size();
    Object fileKey();
}
