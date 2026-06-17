package java.nio.channels.spi;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ClosedChannelException;
import java.io.IOException;

public abstract class AbstractSelectableChannel extends SelectableChannel {
    private final SelectorProvider providerInstance;

    protected AbstractSelectableChannel(SelectorProvider provider) {
        this.providerInstance = provider;
    }

    protected final void implCloseChannel() throws IOException {}
    protected abstract void implCloseSelectableChannel() throws IOException;
    protected abstract void implConfigureBlocking(boolean block) throws IOException;

    @Override public final SelectorProvider provider() { return providerInstance; }
    @Override public final boolean isRegistered() { return false; }
    @Override public final SelectionKey keyFor(Selector sel) { return null; }
    @Override public final SelectionKey register(Selector sel, int ops, Object att) throws ClosedChannelException { return null; }
    @Override public final boolean isBlocking() { return false; }
    @Override public final Object blockingLock() { return this; }
    @Override public final SelectableChannel configureBlocking(boolean block) throws IOException { return this; }
}
