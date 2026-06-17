package com.mojang.authlib.properties;

import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

/**
 * EaglerCraft stub for com.mojang.authlib.properties.PropertyMap.
 * Real class extends ForwardingMultimap<String, Property>. We mirror that
 * so MC code that calls .values() (inherited from Multimap) works.
 */
public class PropertyMap extends ForwardingMultimap<String, PropertyMap.Property> {

    public static final PropertyMap EMPTY = new PropertyMap();

    private final Multimap<String, Property> properties;

    public PropertyMap() {
        this.properties = com.google.common.collect.ArrayListMultimap.create();
    }

    public PropertyMap(Multimap<String, Property> properties) {
        this.properties = properties != null ? properties : com.google.common.collect.ArrayListMultimap.create();
    }

    @Override
    protected Multimap<String, Property> delegate() {
        return properties;
    }

    public Collection<Property> get(String key) {
        return properties.get(key);
    }

    public boolean put(String key, Property property) {
        properties.put(key, property);
        return true;
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public int size() {
        return properties.size();
    }

    /**
     * Property stub.
     */
    public static class Property {
        private final String name;
        private final String value;
        private final String signature;

        public Property(String name, String value) {
            this(name, value, null);
        }

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public String getName() { return name; }
        public String getValue() { return value; }
        public String getSignature() { return signature; }
        public boolean hasSignature() { return signature != null; }
    }
}
