package java.util.concurrent;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * TeaVM stub for java.util.concurrent.ConcurrentSkipListMap.
 * Browser: simple ConcurrentHashMap-backed implementation.
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
        return map.entrySet();
    }

    @Override
    public synchronized K firstKey() { return map.firstKey(); }
    @Override
    public synchronized K lastKey() { return map.lastKey(); }

    @Override
    public synchronized Entry<K, V> firstEntry() { return map.firstEntry(); }
    @Override
    public synchronized Entry<K, V> lastEntry() { return map.lastEntry(); }
    @Override
    public synchronized Entry<K, V> pollFirstEntry() {
        Entry<K, V> e = map.pollFirstEntry();
        return e;
    }
    @Override
    public synchronized Entry<K, V> pollLastEntry() {
        Entry<K, V> e = map.pollLastEntry();
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

    @Override
    public ConcurrentNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>();
        synchronized (this) {
            for (Entry<K, V> e : map.subMap(fromKey, fromInclusive, toKey, toInclusive)) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    @Override
    public ConcurrentNavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>();
        synchronized (this) {
            for (Entry<K, V> e : map.headMap(toKey, inclusive)) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }

    @Override
    public ConcurrentNavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        ConcurrentSkipListMap<K, V> result = new ConcurrentSkipListMap<>();
        synchronized (this) {
            for (Entry<K, V> e : map.tailMap(fromKey, inclusive)) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
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
        synchronized (this) { return map.navigableKeySet(); }
    }

    @Override
    public NavigableSet<K> keySet() {
        synchronized (this) { return map.navigableKeySet(); }
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        synchronized (this) { return map.descendingKeySet(); }
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
