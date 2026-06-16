package java.io;

/**
 * TeaVM stub for java.io.ObjectStreamField.
 */
public class ObjectStreamField {
    private final String name;
    private final Class<?> type;

    public ObjectStreamField(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public Class<?> getType() { return type; }
    public String getTypeString() { return type != null ? type.getName() : ""; }
    public boolean isPrimitive() { return type != null && type.isPrimitive(); }
    public int getOffset() { return 0; }
    public void setOffset(int offset) {}
    public boolean isUnshared() { return false; }
}
