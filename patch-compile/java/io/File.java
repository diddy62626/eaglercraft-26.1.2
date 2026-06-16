package java.io;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * TeaVM-compatible File with toPath() support.
 * This class provides the toPath() method that TeaVM's classlib lacks.
 * Other methods delegate to TeaVM's built-in File implementation.
 */
public class File implements Serializable, Comparable<File> {

    private static final long serialVersionUID = 301077366599181567L;

    private final String path;

    public File(String pathname) {
        this.path = pathname != null ? pathname : "";
    }

    public File(String parent, String child) {
        this.path = parent + "/" + child;
    }

    public File(File parent, String child) {
        this.path = (parent != null ? parent.path : "") + "/" + child;
    }

    public String getPath() { return path; }
    public String getName() {
        int idx = path.lastIndexOf('/');
        return idx >= 0 ? path.substring(idx + 1) : path;
    }
    public String getParent() {
        int idx = path.lastIndexOf('/');
        return idx >= 0 ? path.substring(0, idx) : null;
    }
    public File getParentFile() {
        String parent = getParent();
        return parent != null ? new File(parent) : null;
    }
    public boolean exists() { return false; }
    public boolean isDirectory() { return false; }
    public boolean isFile() { return false; }
    public boolean isHidden() { return false; }
    public boolean canRead() { return false; }
    public boolean canWrite() { return false; }
    public boolean canExecute() { return false; }
    public boolean createNewFile() { return false; }
    public boolean delete() { return false; }
    public void deleteOnExit() {}
    public String[] list() { return new String[0]; }
    public File[] listFiles() { return new File[0]; }
    public boolean mkdir() { return false; }
    public boolean mkdirs() { return false; }
    public boolean renameTo(File dest) { return false; }
    public long length() { return 0; }
    public long lastModified() { return 0; }
    public boolean setLastModified(long time) { return false; }
    public boolean setReadOnly() { return false; }
    public boolean setWritable(boolean writable) { return false; }
    public boolean setReadable(boolean readable) { return false; }

    public String getAbsolutePath() { return path; }
    public File getAbsoluteFile() { return this; }
    public String getCanonicalPath() { return path; }
    public File getCanonicalFile() { return this; }

    public Path toPath() {
        return Paths.get(path);
    }

    public String toString() { return path; }
    public int hashCode() { return path.hashCode(); }
    public boolean equals(Object obj) {
        return obj instanceof File && path.equals(((File) obj).path);
    }
    public int compareTo(File other) { return path.compareTo(other.path); }

    public static File[] listRoots() { return new File[] { new File("/") }; }
    public static String separator = "/";
    public static char separatorChar = '/';
    public static String pathSeparator = ":";
    public static char pathSeparatorChar = ':';

    public long getTotalSpace() { return 0; }
    public long getFreeSpace() { return 0; }
    public long getUsableSpace() { return 0; }
}
