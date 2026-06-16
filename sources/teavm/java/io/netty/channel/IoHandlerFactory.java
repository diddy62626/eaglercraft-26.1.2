package io.netty.channel;

public interface IoHandlerFactory {
    IoHandler newHandler();
    default boolean isChangingThreadSupported() { return false; }
}
