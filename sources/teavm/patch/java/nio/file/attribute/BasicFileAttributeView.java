package java.nio.file.attribute;
public interface BasicFileAttributeView extends FileAttributeView {
    String name();
    BasicFileAttributes readAttributes();
    void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime);
}