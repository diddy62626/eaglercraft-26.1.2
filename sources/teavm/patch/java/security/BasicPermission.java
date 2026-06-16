package java.security;

/**
 * TeaVM stub for java.security.BasicPermission.
 *
 * Browser: no security manager; all permissions are granted.
 */
public abstract class BasicPermission extends Permission {
    private static final long serialVersionUID = 6279438298436172237L;

    public BasicPermission(String name) {
        super(name);
    }

    public BasicPermission(String name, String actions) {
        super(name);
    }

    @Override
    public boolean implies(Permission permission) {
        return true;
    }

    @Override
    public String getActions() {
        return "";
    }
}
