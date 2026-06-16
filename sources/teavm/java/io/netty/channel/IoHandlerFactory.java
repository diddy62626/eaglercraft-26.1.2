package io.netty.channel;

public interface IoHandlerFactory {
    IoHandler newHandler();
}
