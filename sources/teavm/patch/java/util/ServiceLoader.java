package java.util;
public class ServiceLoader<S> implements Iterable<S> {
    public static <S> ServiceLoader<S> load(Class<S> service) { return new ServiceLoader<>(); }
    public java.util.Iterator<S> iterator() { return java.util.Collections.<S>emptyList().iterator(); }
}