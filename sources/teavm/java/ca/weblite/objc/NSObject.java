package ca.weblite.objc;

public class NSObject {
    protected long peer = 0L;
    public NSObject() {}
    public NSObject(long peer) { this.peer = peer; }
    public long getPeer() { return peer; }
    public void release() {}
    public void retain() {}
    public NSObject send(String selector, Object... args) { return this; }
    public Object sendReturnsObject(String selector, Object... args) { return null; }
    public int sendInt(String selector, Object... args) { return 0; }
    public boolean sendBool(String selector, Object... args) { return false; }
    public long sendLong(String selector, Object... args) { return 0L; }
}
