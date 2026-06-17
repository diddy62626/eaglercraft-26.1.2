package java.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TeaVM stub for java.lang.Process.
 * Browser cannot run external processes; all methods throw IOException or return defaults.
 */
public abstract class Process {

    public abstract OutputStream getOutputStream();

    public abstract InputStream getInputStream();

    public abstract InputStream getErrorStream();

    public abstract int waitFor() throws InterruptedException;

    public abstract int exitValue();

    public abstract void destroy();

    public Process destroyForcibly() {
        destroy();
        return this;
    }

    public boolean isAlive() {
        return false;
    }

    /**
     * Stub implementation that always throws IOException.
     */
    public static Process stubOf(String[] cmdarray) throws IOException {
        throw new IOException("Cannot create process in browser environment");
    }
}
