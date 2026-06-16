package java.nio.channels.spi;

import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Channel;

/**
 * TeaVM stub for java.nio.channels.spi.AbstractInterruptibleChannel.
 */
public abstract class AbstractInterruptibleChannel implements Channel, java.io.Closeable {

    private volatile boolean open = true;

    protected AbstractInterruptibleChannel() {
    }

    @Override
    public final boolean isOpen() {
        return open;
    }

    @Override
    public final void close() throws IOException {
        open = false;
        implCloseChannel();
    }

    protected abstract void implCloseChannel() throws IOException;

    protected final void begin() {
    }

    protected final void end(boolean completed) throws AsynchronousCloseException {
    }
}
