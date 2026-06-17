package java.nio.channels.spi;

import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ClosedChannelException;
import java.util.Set;
import java.io.IOException;

public abstract class AbstractSelector extends AbstractInterruptibleChannel implements Selector {
    private final SelectorProvider providerInstance;

    protected AbstractSelector(SelectorProvider provider) {
        this.providerInstance = provider;
    }

    protected abstract void implCloseSelector();
    protected abstract SelectionKey register(SelectableChannel ch, int ops, Object att);

    protected final void cancel(SelectionKey key) {}
    protected final SelectorProvider providerInternal() { return providerInstance; }
    protected final Set<SelectionKey> cancelledKeys() { return new java.util.HashSet<>(); }

    @Override public SelectorProvider provider() { return providerInstance; }
    @Override public Set<SelectionKey> keys() { return new java.util.HashSet<>(); }
    @Override public Set<SelectionKey> selectedKeys() { return new java.util.HashSet<>(); }
    @Override public int selectNow() { return 0; }
    @Override public int select(long timeout) { return 0; }
    @Override public int select() { return 0; }
    @Override public Selector wakeup() { return this; }
    // close() is final in AbstractInterruptibleChannel, inherited as-is
}
