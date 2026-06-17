package java.util.concurrent;

import java.util.NavigableMap;

/**
 * TeaVM stub for ConcurrentNavigableMap.
 */
public interface ConcurrentNavigableMap<K, V> extends ConcurrentMap<K, V>, NavigableMap<K, V> {
    ConcurrentNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);
    ConcurrentNavigableMap<K, V> headMap(K toKey, boolean inclusive);
    ConcurrentNavigableMap<K, V> tailMap(K fromKey, boolean inclusive);
    ConcurrentNavigableMap<K, V> subMap(K fromKey, K toKey);
    ConcurrentNavigableMap<K, V> headMap(K toKey);
    ConcurrentNavigableMap<K, V> tailMap(K fromKey);
    ConcurrentNavigableMap<K, V> descendingMap();
    java.util.NavigableSet<K> navigableKeySet();
    java.util.NavigableSet<K> keySet();
    java.util.NavigableSet<K> descendingKeySet();
}
