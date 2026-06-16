package org.apache.logging.log4j.core.util;

/**
 * TeaVM stub for log4j2 Source.
 * Represents a source for configuration (file/URL/stream). Browser: no-op.
 */
public class Source {
    private final String location;
    private final long lastModified;

    public Source(String location, long lastModified) {
        this.location = location;
        this.lastModified = lastModified;
    }

    public String getLocation() {
        return location;
    }

    public long getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return location;
    }

    @Override
    public int hashCode() {
        return location == null ? 0 : location.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Source)) return false;
        Source other = (Source) obj;
        if (location == null) return other.location == null;
        return location.equals(other.location);
    }
}
