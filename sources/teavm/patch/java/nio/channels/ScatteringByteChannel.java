package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TeaVM stub for java.nio.channels.ScatteringByteChannel.
 */
public interface ScatteringByteChannel extends ReadableByteChannel {

    long read(ByteBuffer[] dsts, int offset, int length) throws IOException;

    long read(ByteBuffer[] dsts) throws IOException;
}
