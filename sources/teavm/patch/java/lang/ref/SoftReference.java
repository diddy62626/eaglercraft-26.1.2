package java.lang.ref;
public class SoftReference<T> extends Reference<T> {
    private T referent;
    public SoftReference(T referent) { this.referent = referent; }
    public SoftReference(T referent, ReferenceQueue<? super T> q) { this.referent = referent; }
    public T get() { return referent; }
}