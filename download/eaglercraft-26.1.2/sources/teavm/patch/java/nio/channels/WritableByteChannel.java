package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TeaVM stub for java.nio.channels.WritableByteChannel.
 */
public interface WritableByteChannel extends Channel {

    int write(ByteBuffer src) throws IOException;
}
