package io.netty.channel.epoll;

public final class Epoll {
    public static boolean isAvailable() { return false; }
    public static boolean ensureAvailability() {}
    public static Throwable unavailabilityCause() { return new UnsupportedOperationException("Epoll not available in browser"); }
}
