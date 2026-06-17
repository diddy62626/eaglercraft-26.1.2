package java.util.concurrent;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;

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

    @Override
    public synchronized V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public synchronized V get(Object key) {
        return map.get(key);
    }

    @Override
    public synchronized V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return new java.util.LinkedHashSet<>(map.entrySet());
    }

    @Override
    public synchronized K firstKey() { return map.firstKey(); }
    @Override
    public synchronized K lastKey() { return map.lastKey(); }

    @Override
    public synchronized Entry<K, V> firstEntry() { return map.isEmpty() ? null : map.firstEntry(); }
    @Override
    public synchronized Entry<K, V> lastEntry() { return map.isEmpty() ? null : map.lastEntry(); }
    @Override
    public synchronized Entry<K, V> pollFirstEntry() {
        if (map.isEmpty()) return null;
        Entry<K, V> e = map.firstEntry();
        map.remove(e.getKey());
        return e;
    }
    @Override
    public synchronized Entry<K, V> pollLastEntry() {
        if (map.isEmpty()) return null;
        Entry<K, V> e = map.lastEntry();
        map.remove(e.getKey());
        return e;
    }

    @Override
    public synchronized Entry<K, V> lowerEntry(K key) { return map.lowerEntry(key); }
    @Override
    public synchronized K lowerKey(K key) { return map.lowerKey(key); }
    @Override
    public synchronized Entry<K, V> floorEntry(K key) { return map.floorEntry(key); }
    @Override
    public synchronized K floorKey(K key) { return map.floorKey(key); }
    @Override
    public synchronized Entry<K, V> ceilingEntry(K key) { return map.ceilingEntry(key); }
    @Override
    public synchronized K ceilingKey(K key) { return map.ceilingKey(key); }
    @Override
    public synchronized Entry<K, V> higherEntry(K key) { return map.higherEntry(key); }
    @Override
    public synchronized K higherKey(K key) { return map.higherKey(key); }

    private ConcurrentSkipListMap<K, V> copyFrom(NavigableMap<K, V> sub) {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>(comparator());
        for (Entry<K, V> e : sub.entrySet()) {
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }

    @Override
    public ConcurrentNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        synchronized (this) {
            return copyFrom(map.subMap(fromKey, fromInclusive, toKey, toInclusive));
        }
    }

    @Override
    public ConcurrentNavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        synchronized (this) {
            return copyFrom(map.headMap(toKey, inclusive));
        }
    }

    @Override
    public ConcurrentNavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        synchronized (this) {
            return copyFrom(map.tailMap(fromKey, inclusive));
        }
    }

    @Override
    public ConcurrentNavigableMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public ConcurrentNavigableMap<K, V> headMap(K toKey) {
        return headMap(toKey, false);
    }

    @Override
    public ConcurrentNavigableMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public ConcurrentNavigableMap<K, V> descendingMap() {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>(Collections.reverseOrder());
        synchronized (this) {
            for (Entry<K, V> e : map.descendingMap().entrySet()) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        synchronized (this) { return new java.util.TreeSet<>(map.keySet()); }
    }

    @Override
    public NavigableSet<K> keySet() {
        synchronized (this) { return new java.util.TreeSet<>(map.keySet()); }
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        synchronized (this) {
            java.util.TreeSet<K> set = new java.util.TreeSet<>(Collections.reverseOrder());
            set.addAll(map.keySet());
            return set;
        }
    }

    @Override
    public Comparator<? super K> comparator() {
        return map.comparator();
    }

    @Override
    public synchronized V putIfAbsent(K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
            return null;
        }
        return map.get(key);
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
            map.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
        if (map.containsKey(key) && Objects.equals(map.get(key), oldValue)) {
            map.put(key, newValue);
            return true;
        }
        return false;
    }

    @Override
    public synchronized V replace(K key, V value) {
        if (map.containsKey(key)) {
            return map.put(key, value);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public ConcurrentSkipListMap<K, V> clone() {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>(comparator());
        synchronized (this) {
            for (Entry<K, V> e : map.entrySet()) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }
}
