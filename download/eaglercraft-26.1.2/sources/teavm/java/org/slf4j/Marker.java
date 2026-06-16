package org.slf4j;

/**
 * Browser-compatible SLF4J Marker stub.
 */
public interface Marker {
    String getName();
    void add(Marker reference);
    boolean remove(Marker reference);
    boolean hasChildren();
    boolean hasReferences();
    boolean contains(Marker other);
    boolean contains(String name);
    Iterable<Marker> iterator();
}
