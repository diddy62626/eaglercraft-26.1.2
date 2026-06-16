package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TeaVM stub for java.nio.channels.GatheringByteChannel.
 */
public interface GatheringByteChannel extends WritableByteChannel {

    long write(ByteBuffer[] srcs, int offset, int length) throws IOException;

    long write(ByteBuffer[] srcs) throws IOException;
}
