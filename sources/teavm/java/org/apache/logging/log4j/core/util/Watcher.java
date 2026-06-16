package org.apache.logging.log4j.core.util;

/**
 * TeaVM stub for log4j2 Watcher interface.
 * Real Watcher monitors a Source for changes. Browser: no-op.
 */
public interface Watcher {
    Source getSource();
    long getLastModified();
    boolean isModified();
    void watching(Source source);
    Watcher newWatcher(Source source, ConfigurationScheduler scheduler, WatchManager watchManager);
}
