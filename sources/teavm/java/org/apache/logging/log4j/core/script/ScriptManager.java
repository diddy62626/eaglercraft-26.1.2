package org.apache.logging.log4j.core.script;

/**
 * TeaVM/browser stub for log4j2's ScriptManager.
 *
 * The real ScriptManager uses javax.script which doesn't have a
 * usable impl in the browser. This stub skips all script execution
 * so log4j configurations that reference scripts simply ignore them.
 */
public class ScriptManager {
    public void addScript(AbstractScript script) {
        // No-op: scripts are not executed in browser environment
    }

    public ScriptRunner createScriptRunner() {
        return new NoopScriptRunner();
    }

    public interface ScriptRunner {
        Object executeScript(String name);
        Object executeScript(String name, Object[] args);
    }

    public abstract static class AbstractScript {
        private final String name;
        private final String scriptText;
        private final String language;

        public AbstractScript(String name, String language, String scriptText) {
            this.name = name;
            this.language = language;
            this.scriptText = scriptText;
        }

        public String getName() { return name; }
        public String getScriptText() { return scriptText; }
        public String getLanguage() { return language; }
        public abstract String toString();
    }

    private static class NoopScriptRunner implements ScriptRunner {
        @Override public Object executeScript(String name) { return null; }
        @Override public Object executeScript(String name, Object[] args) { return null; }
    }
}
