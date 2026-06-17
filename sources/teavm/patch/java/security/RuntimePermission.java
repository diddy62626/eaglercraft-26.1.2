package java.security;

/**
 * TeaVM stub for RuntimePermission.
 * Browser environment doesn't enforce Java security permissions.
 */
public final class RuntimePermission extends java.security.BasicPermission {
    private static final long serialVersionUID = 7399184964622342223L;

    public RuntimePermission(String name) {
        super(name);
    }

    public RuntimePermission(String name, String actions) {
        super(name, actions);
    }
}
