package org.apache.logging.log4j.core.script;

import org.apache.logging.log4j.core.config.Configuration;

/**
 * TeaVM/browser stub for log4j2's ScriptManager.
 *
 * The real ScriptManager uses javax.script which doesn't have a
 * usable impl in the browser. This stub skips all script execution.
 *
 * Note: AbstractScript is the real log4j-core top-level class
 * (org.apache.logging.log4j.core.script.AbstractScript), NOT an
 * inner class. The MC code calls addScript(realAbstractScript).
 */
public class ScriptManager {
    private final Configuration configuration;
    private final String language;
    private final java.util.Map<String, AbstractScript> scripts = new java.util.concurrent.ConcurrentHashMap<>();

    public ScriptManager(Configuration configuration, String language) {
        this.configuration = configuration;
        this.language = language;
    }

    public ScriptManager(Configuration configuration,
                         org.apache.logging.log4j.core.util.WatchManager watchManager,
                         String language) {
        this.configuration = configuration;
        this.language = language;
    }

    public boolean addScript(AbstractScript script) {
        if (script != null) {
            scripts.put(script.getName(), script);
        }
        return true;
    }

    public AbstractScript getScript(String name) {
        return scripts.get(name);
    }

    public ScriptRunner createScriptRunner() {
        return new NoopScriptRunner();
    }

    public interface ScriptRunner {
        Object executeScript(String name);
        Object executeScript(String name, Object[] args);
    }

    private static class NoopScriptRunner implements ScriptRunner {
        @Override public Object executeScript(String name) { return null; }
        @Override public Object executeScript(String name, Object[] args) { return null; }
    }
}
