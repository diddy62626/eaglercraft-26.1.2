package java.nio.file.attribute;

import java.util.Set;

public interface PosixFileAttributes extends BasicFileAttributes {
    java.nio.file.attribute.UserPrincipal owner();
    java.nio.file.attribute.GroupPrincipal group();
    Set<PosixFilePermission> permissions();
}
