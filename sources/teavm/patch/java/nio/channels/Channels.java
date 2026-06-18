package java.nio.channels;
public class Channels {
    public static java.io.InputStream newInputStream(ReadableByteChannel ch) { return new java.io.ByteArrayInputStream(new byte[0]); }
    public static java.io.OutputStream newOutputStream(WritableByteChannel ch) { return new java.io.ByteArrayOutputStream(); }

// === Plugin-injected methods ===
    public static java.nio.channels.WritableByteChannel newChannel(java.io.OutputStream out) { return null; }
    public static java.io.Writer newWriter(java.nio.channels.WritableByteChannel ch, java.nio.charset.Charset charset) { return new java.io.StringWriter(); }
}