package jdk.jfr;

public abstract class Event {
    public void begin() {}
    public void end() {}
    public void commit() {}
    public boolean isEnabled() { return false; }
    public boolean shouldCommit() { return false; }
    public void set(int index, Object value) {}
    public Object get(int index) { return null; }
}
