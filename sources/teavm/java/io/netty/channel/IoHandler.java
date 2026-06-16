package io.netty.channel;

public interface IoHandler {
    default void wakeup() {}
}
