package java.nio.file;
public class SimpleFileVisitor<T> implements FileVisitor<T> {
    public FileVisitResult preVisitDirectory(T dir, java.nio.file.attribute.BasicFileAttributes attrs) { return FileVisitResult.CONTINUE; }
    public FileVisitResult visitFile(T file, java.nio.file.attribute.BasicFileAttributes attrs) { return FileVisitResult.CONTINUE; }
    public FileVisitResult visitFileFailed(T file, java.io.IOException exc) { return FileVisitResult.CONTINUE; }
    public FileVisitResult postVisitDirectory(T dir, java.io.IOException exc) { return FileVisitResult.CONTINUE; }
}