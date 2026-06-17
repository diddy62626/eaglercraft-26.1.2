package java.security;

/**
 * TeaVM stub for java.security.PermissionCollection.
 */
public abstract class PermissionCollection implements java.io.Serializable {
    private static final long serialVersionUID = -3727486164799283439L;

    public abstract void add(Permission permission);
    public abstract java.util.Enumeration<Permission> elements();
    public abstract boolean implies(Permission permission);

    public void setReadOnly() {}
    public boolean isReadOnly() { return false; }
}
