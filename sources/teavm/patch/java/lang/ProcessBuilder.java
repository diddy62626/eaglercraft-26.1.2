package java.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * TeaVM stub for java.lang.ProcessBuilder.
 * Browser cannot run external processes; start() always throws IOException.
 */
public class ProcessBuilder {

    private List<String> command;
    private File directory;
    private Map<String, String> environment;
    private boolean redirectErrorStream;

    public ProcessBuilder(List<String> command) {
        this.command = new ArrayList<>(command);
    }

    public ProcessBuilder(String... command) {
        this.command = new ArrayList<>(Arrays.asList(command));
    }

    public ProcessBuilder command(List<String> command) {
        this.command = new ArrayList<>(command);
        return this;
    }

    public ProcessBuilder command(String... command) {
        this.command = new ArrayList<>(Arrays.asList(command));
        return this;
    }

    public List<String> command() {
        return command;
    }

    public Map<String, String> environment() {
        if (environment == null) {
            environment = new java.util.HashMap<>();
        }
        return environment;
    }

    public File directory() {
        return directory;
    }

    public ProcessBuilder directory(File directory) {
        this.directory = directory;
        return this;
    }

    public boolean redirectErrorStream() {
        return redirectErrorStream;
    }

    public ProcessBuilder redirectErrorStream(boolean redirectErrorStream) {
        this.redirectErrorStream = redirectErrorStream;
        return this;
    }

    /**
     * Always throws IOException in browser environment.
     */
    public Process start() throws IOException {
        throw new IOException("Cannot start process in browser environment");
    }

    /**
     * Always throws IOException in browser environment.
     */
    public Process start(Map<String, String> env) throws IOException {
        throw new IOException("Cannot start process in browser environment");
    }

    // -- Redirect inner class stub --

    public static class Redirect {
        public static final Redirect INHERIT = new Redirect();
        public static final Redirect PIPE = new Redirect();
        public static final Redirect DISCARD = new Redirect();

        private Redirect() {}

        public static Redirect from(File file) { return PIPE; }
        public static Redirect to(File file) { return PIPE; }
        public static Redirect appendTo(File file) { return PIPE; }

        public File file() { return null; }
        public Type type() { return Type.PIPE; }

        public enum Type {
            PIPE, INHERIT, READ, WRITE, APPEND
        }
    }

    public ProcessBuilder redirectInput(File file) { return this; }
    public ProcessBuilder redirectOutput(File file) { return this; }
    public ProcessBuilder redirectError(File file) { return this; }
    public ProcessBuilder redirectInput(Redirect source) { return this; }
    public ProcessBuilder redirectOutput(Redirect destination) { return this; }
    public ProcessBuilder redirectError(Redirect destination) { return this; }
    public Redirect redirectInput() { return Redirect.PIPE; }
    public Redirect redirectOutput() { return Redirect.PIPE; }
    public Redirect redirectError() { return Redirect.PIPE; }
}
