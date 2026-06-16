package java.nio.file;

/**
 * TeaVM-compatible stub for java.nio.file.Paths.
 */
public final class Paths {
    private Paths() {}

    public static Path get(String first, String... more) {
        return Path.of(first, more);
    }

    public static Path get(java.net.URI uri) {
        return Path.of(uri);
    }
}
