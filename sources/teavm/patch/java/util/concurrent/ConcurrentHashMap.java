package java.util.concurrent;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * TeaVM stub for java.util.concurrent.ConcurrentHashMap.
 * Delegates to regular HashMap since browser is single-threaded.
 */
public class ConcurrentHashMap<K, V> implements ConcurrentMap<K, V>, java.io.Serializable {

    private final HashMap<K, V> map;

    public ConcurrentHashMap() {
        this.map = new HashMap<>();
    }

    public ConcurrentHashMap(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    public ConcurrentHashMap(int initialCapacity, float loadFactor) {
        this.map = new HashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.map = new HashMap<>(initialCapacity, loadFactor);
    }

    public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
        this.map = new HashMap<>(m);
    }

    // -- ConcurrentMap methods --

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        V v = map.get(key);
        return v != null || containsKey(key) ? v : defaultValue;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    @Override
    public boolean remove(Object key, Object value) {
        Object old = map.get(key);
        if (old != null && value != null && old.equals(value)) {
            map.remove(key);
            return true;
        }
        if (old == null && value == null && map.containsKey(key)) {
            map.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Object cur = map.get(key);
        if (cur != null && cur.equals(oldValue)) {
            map.put(key, newValue);
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        if (map.containsKey(key)) {
            return map.put(key, value);
        }
        return null;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V v = map.get(key);
        if (v == null) {
            v = map.put(key, value);
        }
        return v;
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        V v = map.get(key);
        if (v == null) {
            V newValue = mappingFunction.apply(key);
            if (newValue != null) {
                map.put(key, newValue);
            }
            return newValue;
        }
        return v;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V oldValue = map.get(key);
        if (oldValue != null) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                map.put(key, newValue);
                return newValue;
            } else {
                map.remove(key);
                return null;
            }
        }
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V oldValue = map.get(key);
        V newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            if (oldValue != null || map.containsKey(key)) {
                map.remove(key);
            }
            return null;
        } else {
            map.put(key, newValue);
            return newValue;
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V oldValue = map.get(key);
        V newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            map.remove(key);
        } else {
            map.put(key, newValue);
        }
        return newValue;
    }

    // -- Map delegation --

    @Override
    public int size() { return map.size(); }

    @Override
    public boolean isEmpty() { return map.isEmpty(); }

    @Override
    public boolean containsKey(Object key) { return map.containsKey(key); }

    @Override
    public boolean containsValue(Object value) { return map.containsValue(value); }

    @Override
    public V get(Object key) { return map.get(key); }

    @Override
    public V put(K key, V value) { return map.put(key, value); }

    @Override
    public V remove(Object key) { return map.remove(key); }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) { map.putAll(m); }

    @Override
    public void clear() { map.clear(); }

    @Override
    public Set<K> keySet() { return map.keySet(); }

    @Override
    public Collection<V> values() { return map.values(); }

    @Override
    public Set<Map.Entry<K, V>> entrySet() { return map.entrySet(); }

    @Override
    public boolean equals(Object o) { return map.equals(o); }

    @Override
    public int hashCode() { return map.hashCode(); }

    @Override
    public String toString() { return map.toString(); }

    // -- ConcurrentHashMap-specific methods --

    public Enumeration<K> keys() {
        return new Enumeration<K>() {
            private final Iterator<K> it = keySet().iterator();
            @Override public boolean hasMoreElements() { return it.hasNext(); }
            @Override public K nextElement() { return it.next(); }
        };
    }

    public Enumeration<V> elements() {
        return new Enumeration<V>() {
            private final Iterator<V> it = values().iterator();
            @Override public boolean hasMoreElements() { return it.hasNext(); }
            @Override public V nextElement() { return it.next(); }
        };
    }

    public long mappingCount() {
        return map.size();
    }

    public KeySetView<K, V> keySet(V mappedValue) {
        return new KeySetView<K, V>(this, mappedValue);
    }

    /**
     * Inner stub for KeySetView.
     */
    public static class KeySetView<K, V> extends java.util.AbstractSet<K> {
        private final ConcurrentHashMap<K, V> map;

        KeySetView(ConcurrentHashMap<K, V> map, V mappedValue) {
            this.map = map;
        }

        @Override
        public int size() { return map.size(); }

        @Override
        public boolean contains(Object o) { return map.containsKey(o); }

        @Override
        public Iterator<K> iterator() { return map.keySet().iterator(); }

        @Override
        public boolean add(K e) {
            return map.putIfAbsent(e, null) == null;
        }

        @Override
        public boolean remove(Object o) {
            return map.remove(o) != null;
        }

        public boolean addAll(Collection<? extends K> c) {
            boolean added = false;
            for (K e : c) {
                if (add(e)) added = true;
            }
            return added;
        }
    }

    public static <K> java.util.Set<K> newKeySet() {
        return new java.util.concurrent.ConcurrentHashMap<K, Boolean>().keySet();
    }
    public static <K> java.util.Set<K> newKeySet(int initialCapacity) {
        return new java.util.concurrent.ConcurrentHashMap<K, Boolean>(initialCapacity).keySet();
    }
    public K search(long parallelismThreshold, java.util.function.BiFunction<? super K, ? super V, ? extends K> searchFunction) {
        return null;
    }
    public K searchKeys(long parallelismThreshold, java.util.function.Function<? super K, ? extends K> searchFunction) {
        return null;
    }
    public V searchValues(long parallelismThreshold, java.util.function.Function<? super V, ? extends V> searchFunction) {
        return null;
    }
    public void forEach(long parallelismThreshold, java.util.function.BiConsumer<? super K, ? super V> action) {
        forEach(action);
    }

}
