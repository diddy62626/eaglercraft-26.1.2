package org.slf4j;

/**
 * Browser-compatible SLF4J MarkerFactory stub.
 */
public class MarkerFactory {

    private static final Marker NOP_MARKER = new NOPMarker();

    public static Marker getMarker(String name) {
        return NOP_MARKER;
    }

    public static Marker getDetachedMarker(String name) {
        return NOP_MARKER;
    }

    private static class NOPMarker implements Marker {
        @Override public String getName() { return "NOP"; }
        @Override public void add(Marker reference) {}
        @Override public boolean remove(Marker reference) { return false; }
        @Override public boolean hasChildren() { return false; }
        @Override public boolean hasReferences() { return false; }
        @Override public boolean contains(Marker other) { return false; }
        @Override public boolean contains(String name) { return false; }
        @Override public Iterable<Marker> iterator() { return java.util.Collections.emptyList(); }
    }
}
