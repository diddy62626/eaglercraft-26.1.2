package java.net.http;
public class BodyPublishers {
    public static BodyPublisher noBody() { return null; }
    public static BodyPublisher ofString(String body) { return null; }
    public static BodyPublisher ofByteArray(byte[] buf) { return null; }
    public static BodyPublisher ofInputStream(java.util.function.Supplier<? extends java.io.InputStream> streamSupplier) { return null; }
}