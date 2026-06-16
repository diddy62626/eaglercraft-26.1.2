package java.lang;

/**
 * TeaVM stub for java.lang.RuntimePermission.
 *
 * NOTE: Real Java has RuntimePermission in java.security, but
 * log4j 2.25.2's LoaderUtil explicitly references java.lang.RuntimePermission.
 * We provide this stub at that location to satisfy TeaVM compilation.
 *
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
