package org.osgi.framework;

/**
 * TeaVM stub for OSGi Version.
 */
public class Version implements Comparable<Version> {
    private final int major;
    private final int minor;
    private final int micro;
    private final String qualifier;

    public static final Version emptyVersion = new Version(0, 0, 0);

    public Version(int major, int minor, int micro) {
        this(major, minor, micro, null);
    }

    public Version(int major, int minor, int micro, String qualifier) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.qualifier = qualifier == null ? "" : qualifier;
    }

    public static Version parseVersion(String version) {
        return emptyVersion;
    }

    public static Version valueOf(String version) {
        return parseVersion(version);
    }

    public int getMajor() { return major; }
    public int getMinor() { return minor; }
    public int getMicro() { return micro; }
    public String getQualifier() { return qualifier; }

    @Override
    public int compareTo(Version other) {
        if (major != other.major) return major - other.major;
        if (minor != other.minor) return minor - other.minor;
        if (micro != other.micro) return micro - other.micro;
        return qualifier.compareTo(other.qualifier);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + micro + (qualifier.isEmpty() ? "" : "." + qualifier);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Version && toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
