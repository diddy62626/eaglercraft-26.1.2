package net.minecraft.resources;

import java.util.Optional;
import java.util.function.UnaryOperator;

public final class Identifier {
    private final String namespace;
    private final String path;

    public static final com.mojang.serialization.Codec<Identifier> CODEC = new com.mojang.serialization.Codec<Identifier>() {
        @Override public Identifier decode(Object input) { return new Identifier(String.valueOf(input)); }
        @Override public Object encode(Identifier value) { return value.toString(); }
    };

    public static final Object STREAM_CODEC = null;

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public Identifier(String full) {
        int idx = full.indexOf(':');
        if (idx >= 0) {
            this.namespace = full.substring(0, idx);
            this.path = full.substring(idx + 1);
        } else {
            this.namespace = "minecraft";
            this.path = full;
        }
    }

    public String getNamespace() { return namespace; }
    public String getPath() { return path; }

    // Static factory methods
    public static Identifier parse(String text) { return new Identifier(text); }
    public static Identifier tryParse(String text) {
        try { return new Identifier(text); } catch (Exception e) { return null; }
    }
    public static Optional<Identifier> tryParseOptional(String text) {
        try { return Optional.of(new Identifier(text)); } catch (Exception e) { return Optional.empty(); }
    }
    public static Identifier tryBuild(String namespace, String path) {
        try { return new Identifier(namespace, path); } catch (Exception e) { return null; }
    }
    public static Identifier fromNamespaceAndPath(String namespace, String path) { return new Identifier(namespace, path); }
    public static Identifier bySeparator(String text, char separator) {
        int idx = text.indexOf(separator);
        if (idx >= 0) return new Identifier(text.substring(0, idx), text.substring(idx + 1));
        return new Identifier("minecraft", text);
    }
    public static Identifier withDefaultNamespace(String path) { return new Identifier("minecraft", path); }

    public static boolean isValidNamespace(String namespace) {
        return namespace != null && namespace.matches("[a-z0-9_.-]+");
    }
    public static boolean isValidPath(String path) {
        return path != null && path.matches("[a-z0-9_./-]+");
    }

    // Instance methods
    public Identifier withPath(String path) { return new Identifier(namespace, path); }
    public Identifier withPath(UnaryOperator<String> fn) { return new Identifier(namespace, fn.apply(path)); }
    public Identifier withPrefix(String prefix) { return new Identifier(namespace, prefix + path); }
    public Identifier withSuffix(String suffix) { return new Identifier(namespace, path + suffix); }
    public String toShortLanguageKey() { return path; }

    @Override public String toString() { return namespace + ":" + path; }
    @Override public int hashCode() { return toString().hashCode(); }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Identifier)) return false;
        return toString().equals(o.toString());
    }

    public String toShortString() { return path; }
}
