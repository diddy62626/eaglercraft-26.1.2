package java.nio.file;
import java.nio.file.attribute.BasicFileAttributes;
public interface FileVisitor<T> {
    FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs);
    FileVisitResult visitFile(T file, BasicFileAttributes attrs);
    FileVisitResult visitFileFailed(T file, java.io.IOException exc);
    FileVisitResult postVisitDirectory(T dir, java.io.IOException exc);
}