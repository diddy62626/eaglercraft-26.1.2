package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.util.WatchManager;

/**
 * TeaVM/browser stub for log4j2's ScriptManager.
 *
 * The real ScriptManager uses javax.script which doesn't have a
 * usable impl in the browser. This stub skips all script execution.
 */
public class ScriptManager {
    private final Configuration configuration;
    private final WatchManager watchManager;
    private final String language;

    public ScriptManager(Configuration configuration, WatchManager watchManager, String language) {
        this.configuration = configuration;
        this.watchManager = watchManager;
        this.language = language;
    }

    public boolean addScript(AbstractScript script) {
        // No-op: scripts are not executed in browser environment
        return true;
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
