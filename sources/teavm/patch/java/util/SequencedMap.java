package java.util;
public interface SequencedMap<K,V> extends Map<K,V> {
    SequencedMap<K,V> reversed();
    Map.Entry<K,V> firstEntry();
    Map.Entry<K,V> lastEntry();
}