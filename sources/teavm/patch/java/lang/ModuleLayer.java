package java.lang;
public final class ModuleLayer {
    public static ModuleLayer boot() { return new ModuleLayer(); }
    public Module findModule(String name) { return null; }
}