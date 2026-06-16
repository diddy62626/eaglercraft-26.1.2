package java.lang;

/**
 * TeaVM stub for java.lang.Runtime.
 * Browser-compatible: no native library loading, no process execution.
 */
public class Runtime {
    private static final Runtime INSTANCE = new Runtime();

    private Runtime() {}

    public static Runtime getRuntime() { return INSTANCE; }

    public int availableProcessors() { return 1; }

    public long freeMemory() { return 128L * 1024 * 1024; }

    public long totalMemory() { return 256L * 1024 * 1024; }

    public long maxMemory() { return 512L * 1024 * 1024; }

    public void gc() {}

    public void runFinalization() {}

    public void traceInstructions(boolean on) {}

    public void traceMethodCalls(boolean on) {}

    public void load(String filename) {
        throw new UnsatisfiedLinkError("Cannot load native library in browser: " + filename);
    }

    public void loadLibrary(String libname) {
        throw new UnsatisfiedLinkError("Cannot load native library in browser: " + libname);
    }

    public Process exec(String command) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public Process exec(String[] cmdarray) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public Process exec(String[] cmdarray, String[] envp) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public Process exec(String[] cmdarray, String[] envp, java.io.File dir) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public Process exec(String command, String[] envp) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public Process exec(String command, String[] envp, java.io.File dir) throws java.io.IOException {
        throw new java.io.IOException("Cannot execute processes in browser");
    }

    public void exit(int status) {
        // No Runtime.exit() in browser
    }

    public void addShutdownHook(Thread hook) {
        // No shutdown hooks in browser
    }

    public boolean removeShutdownHook(Thread hook) {
        return false;
    }

    public void halt(int status) {
        // No halt in browser
    }

    // Runtime.Version not available in this patch context



    public java.util.Map<String,String> getenv() {
        return java.util.Collections.emptyMap();
    }

    public String getenv(String name) { return null; }

    public java.io.InputStream getLocalizedInputStream(java.io.InputStream in) { return in; }

    public java.io.OutputStream getLocalizedOutputStream(java.io.OutputStream out) { return out; }
}
