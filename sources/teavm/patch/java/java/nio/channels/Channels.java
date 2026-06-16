package java.nio.channels;
public class Channels {
    public static java.io.InputStream newInputStream(ReadableByteChannel ch) { return new java.io.ByteArrayInputStream(new byte[0]); }
    public static java.io.OutputStream newOutputStream(WritableByteChannel ch) { return new java.io.ByteArrayOutputStream(); }
}