package java.nio.channels;

import java.io.IOException;

/**
 * TeaVM stub for java.nio.channels.Channel.
 */
public interface Channel extends AutoCloseable {

    boolean isOpen();

    @Override
    void close() throws IOException;
}
