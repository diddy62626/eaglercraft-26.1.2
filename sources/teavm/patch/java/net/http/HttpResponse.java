package java.net.http;
public interface HttpResponse<T> {
    int statusCode();
    HttpRequest request();
    HttpHeaders headers();
    T body();
    interface BodyHandler<T> {
        BodySubscriber<T> apply(ResponseInfo responseInfo);
    }
    class BodyHandlers {
        public static BodyHandler<String> ofString() { return null; }
        public static BodyHandler<byte[]> ofByteArray() { return null; }
        public static BodyHandler<java.io.InputStream> ofInputStream() { return null; }
        public static BodyHandler<Void> discarding() { return null; }
    }
    interface BodySubscriber<T> {}
    interface ResponseInfo {
        int statusCode();
        HttpHeaders headers();
        HttpClient.Version version();
    }
}
