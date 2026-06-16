package java.nio.channels;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * TeaVM stub for java.nio.channels.Selector.
 * Declared as interface so that AbstractSelector can `implements Selector`.
 *
 * (Real Java has Selector as abstract class, but our stub uses interface
 * to avoid Java's single-inheritance restriction with AbstractInterruptibleChannel.)
 */
public interface Selector extends Closeable {
    static Selector open() throws IOException {
        return null; // Browser: no selector support
    }

    boolean isOpen();
    SelectorProvider provider();
    Set<SelectionKey> keys();
    Set<SelectionKey> selectedKeys();
    int selectNow() throws IOException;
    int select(long timeout) throws IOException;
    int select() throws IOException;
    Selector wakeup();
    @Override void close() throws IOException;
}
