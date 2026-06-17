package java.lang;

public final class Package {
    private final String name;

    private Package(String name) { this.name = name; }

    public String getName() { return name; }
    public String getSpecificationTitle() { return null; }
    public String getSpecificationVersion() { return null; }
    public String getSpecificationVendor() { return null; }
    public String getImplementationTitle() { return null; }
    public String getImplementationVersion() { return null; }
    public String getImplementationVendor() { return null; }
    public boolean isSealed() { return false; }
    public boolean isSealed(java.net.URL url) { return false; }
    public boolean isCompatibleWith(String desired) { return true; }

    public static Package getPackage(String name) {
        return name != null ? new Package(name) : null;
    }
    public static Package[] getPackages() { return new Package[0]; }

    public int hashCode() { return name.hashCode(); }
    public String toString() { return "package " + name; }
}
