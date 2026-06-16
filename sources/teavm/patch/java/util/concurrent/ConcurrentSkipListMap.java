package java.util.concurrent;

import java.util.*;

/**
 * TeaVM stub for java.util.concurrent.ConcurrentSkipListMap.
 * Simple synchronized TreeMap-backed implementation.
 */
public class ConcurrentSkipListMap<K, V> extends AbstractMap<K, V>
        implements ConcurrentNavigableMap<K, V>, Cloneable, java.io.Serializable {

    private final TreeMap<K, V> map;

    public ConcurrentSkipListMap() {
        this.map = new TreeMap<>();
    }

    @SuppressWarnings("unchecked")
    public ConcurrentSkipListMap(Comparator<? super K> comparator) {
        this.map = new TreeMap<>(comparator);
    }

    public ConcurrentSkipListMap(Map<? extends K, ? extends V> m) {
        this.map = new TreeMap<>();
        putAll(m);
    }


}
