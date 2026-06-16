package java.nio.channels.spi;

import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Set;

public abstract class AbstractSelector extends java.nio.channels.spi.AbstractInterruptibleChannel implements Selector {
    protected AbstractSelector(SelectorProvider provider) {}

    protected abstract void implCloseSelector();
    protected abstract SelectionKey register(java.nio.channels.SelectableChannel ch, int ops, Object att);

    protected final void cancel(SelectionKey key) {}
    protected final SelectorProvider provider() { return null; }
    protected final Set<SelectionKey> cancelledKeys() { return new java.util.HashSet<>(); }
    protected final void begin() {}
    protected final void end() {}
    protected final boolean isOpen() { return true; }

    @Override public java.nio.channels.SelectorProvider provider() { return null; }
    @Override public Set<SelectionKey> keys() { return new java.util.HashSet<>(); }
    @Override public Set<SelectionKey> selectedKeys() { return new java.util.HashSet<>(); }
    @Override public int selectNow() { return 0; }
    @Override public int select(long timeout) { return 0; }
    @Override public int select() { return 0; }
    @Override public Selector wakeup() { return this; }
    @Override public void close() {}
}
