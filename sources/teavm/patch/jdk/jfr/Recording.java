package jdk.jfr;

public final class Recording implements java.io.Closeable {
    public Recording() {}
    public Recording(java.util.Map<String, String> settings) {}
    public void start() {}
    public void stop() {}
    @Override public void close() {}
    public boolean isOpen() { return false; }
    public Recording copy(boolean stop) { return new Recording(); }
    public void dump(java.nio.file.Path destination) {}
    public void addToStartup() {}
    public void dump(java.io.OutputStream stream) {}
    public void scheduleStart(java.time.Instant startTime) {}
    public long getSize() { return 0L; }
    public long getMaxSize() { return 0L; }
    public void setMaxSize(long maxSize) {}
    public java.time.Duration getMaxAge() { return java.time.Duration.ZERO; }
    public void setMaxAge(java.time.Duration maxAge) {}
    public void enable(String name) {}
    public void disable(String name) {}
    public void setSettings(java.util.Map<String, String> settings) {}
    public java.util.Map<String, String> getSettings() { return new java.util.HashMap<>(); }
    public long getId() { return 0L; }
    public String getName() { return ""; }
    public void setName(String name) {}
    public java.time.Instant getStartTime() { return null; }
    public java.time.Instant getStopTime() { return null; }
    public long getDuration() { return 0L; }
    public State getState() { return State.NEW; }

    public enum State { NEW, DELAYED, RUNNING, STOPPED, CLOSED; }
}
