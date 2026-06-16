package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TeaVM stub for java.nio.channels.ReadableByteChannel.
 */
public interface ReadableByteChannel extends Channel {

    int read(ByteBuffer dst) throws IOException;
}
