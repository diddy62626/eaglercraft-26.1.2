package org.apache.logging.log4j.core.util;

import java.util.List;

import org.apache.logging.log4j.core.config.ConfigurationScheduler;
import org.apache.logging.log4j.core.config.Reconfigurable;

/**
 * TeaVM stub for log4j2 Watcher interface.
 * Real Watcher monitors a Source for changes. Browser: no-op.
 */
public interface Watcher {
    Source getSource();
    long getLastModified();
    boolean isModified();
    void watching(Source source);

    /**
     * MC 26.1.2 / log4j 2.25.2: Static factory used by WatchManager.
     */
    Watcher newWatcher(Source source, ConfigurationScheduler scheduler, WatchManager watchManager);

    /**
     * log4j 2.25.2: Alternative factory signature used by Reconfigurable configs.
     */
    Watcher newWatcher(Reconfigurable reconfigurable, List<ConfigurationScheduler> schedulers, long lastModifiedMillis);
}
