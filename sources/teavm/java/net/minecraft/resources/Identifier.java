package net.minecraft.resources;

public final class Identifier {
    private final String namespace;
    private final String path;

    public static final com.mojang.serialization.Codec<Identifier> CODEC = new com.mojang.serialization.Codec<Identifier>() {
        @Override public Identifier decode(Object input) { return new Identifier(String.valueOf(input)); }
        @Override public Object encode(Identifier value) { return value.toString(); }
    };

    public static final Object STREAM_CODEC = null; // stub

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

    @Override public String toString() { return namespace + ":" + path; }
    @Override public int hashCode() { return toString().hashCode(); }
    @Override public boolean equals(Object o) {
        if (!(o instanceof Identifier)) return false;
        return toString().equals(o.toString());
    }
}
