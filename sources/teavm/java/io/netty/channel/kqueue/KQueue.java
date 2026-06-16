package io.netty.channel.kqueue;

public final class KQueue {
    public static boolean isAvailable() { return false; }
    public static boolean ensureAvailability() { return false; }
    public static Throwable unavailabilityCause() { return new UnsupportedOperationException("KQueue not available in browser"); }
}
