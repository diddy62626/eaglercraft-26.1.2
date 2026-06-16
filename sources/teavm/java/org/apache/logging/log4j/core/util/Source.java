package org.apache.logging.log4j.core.util;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * TeaVM stub for log4j2 Source.
 * Represents a source for configuration (file/URL/stream). Browser: no-op.
 *
 * Real log4j 2.25.2 Source has 4 constructors (File, URI, URL, plus the
 * (location, lastModified) constructor we already had) and a getURI() method.
 */
public class Source {
    private final String location;
    private final long lastModified;
    private final URI uri;

    public Source(String location, long lastModified) {
        this.location = location;
        this.lastModified = lastModified;
        this.uri = null;
    }

    public Source(File file) {
        this.location = file == null ? null : file.getAbsolutePath();
        this.lastModified = file == null ? 0L : file.lastModified();
        this.uri = file == null ? null : file.toURI();
    }

    public Source(URI uri) {
        this.location = uri == null ? null : uri.toString();
        this.lastModified = 0L;
        this.uri = uri;
    }

    public Source(URL url) {
        this.location = url == null ? null : url.toString();
        this.lastModified = 0L;
        this.uri = null;
    }

    public String getLocation() {
        return location;
    }

    public long getLastModified() {
        return lastModified;
    }

    public URI getURI() {
        return uri;
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

    public java.io.File getFile() {
        return uri == null ? null : new java.io.File(uri);
    }
}
