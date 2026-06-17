package java.security;

/**
 * TeaVM stub for java.security.Permission.
 *
 * Browser: no security manager; all permissions are granted.
 */
public abstract class Permission implements java.security.Guard, java.io.Serializable {
    private static final long serialVersionUID = -5636570222231596674L;

    private final String name;

    public Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean implies(Permission permission);

    public abstract String getActions();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Permission)) return false;
        Permission other = (Permission) obj;
        if (name == null) return other.name == null;
        return name.equals(other.name);
    }

    @Override
    public void checkGuard(Object object) throws SecurityException {
        // no-op in browser
    }

    public java.security.PermissionCollection newPermissionCollection() {
        return null;
    }
}
