package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TeaVM stub for java.nio.channels.SeekableByteChannel.
 */
public interface SeekableByteChannel extends ByteChannel {

    long position() throws IOException;

    SeekableByteChannel position(long newPosition) throws IOException;

    long size() throws IOException;

    SeekableByteChannel truncate(long size) throws IOException;
}
