package org.apache.logging.log4j.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.core.config.ConfigurationScheduler;

/**
 * EaglerCraft stub for log4j2 WatchManager.
 *
 * The real WatchManager monitors configuration files for changes via a
 * background thread. In the browser we don't watch files, so all
 * operations are no-ops that return sensible defaults.
 */
public class WatchManager {
    private final ConfigurationScheduler scheduler;
    private final Map<Object, Object> configurationWatchers = new ConcurrentHashMap<>();
    private int intervalSeconds = 30;
    private boolean started = false;

    public WatchManager(ConfigurationScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public WatchManager() {
        this.scheduler = null;
    }

    public void start() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void setIntervalSeconds(int intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    public Map<org.apache.logging.log4j.core.util.Source, org.apache.logging.log4j.core.util.Watcher> getConfigurationWatchers() {
        return (Map) configurationWatchers;
    }

    public void watch(Source source, Watcher watcher) {
        if (source != null && watcher != null) {
            configurationWatchers.put(source, watcher);
        }
    }

    public void unwatch(Source source) {
        if (source != null) {
            configurationWatchers.remove(source);
        }
    }

    public boolean stop(long timeout, TimeUnit timeUnit) {
        started = false;
        configurationWatchers.clear();
        return true;
    }

    public void stop() {
        started = false;
        configurationWatchers.clear();
    }
}
