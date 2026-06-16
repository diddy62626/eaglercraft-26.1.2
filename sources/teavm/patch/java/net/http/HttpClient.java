package java.net.http;
public class HttpClient {
    public static class Builder {
        public Builder followRedirects(Redirect policy) { return this; }
        public Builder version(Version version) { return this; }
        public HttpClient build() { return new HttpClient(); }
    }
    public enum Redirect { NEVER, NORMAL, ALWAYS }
    public enum Version { HTTP_1_1, HTTP_2 }
    public static Builder newBuilder() { return new Builder(); }
    public static HttpClient newHttpClient() { return new HttpClient(); }
    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) { return null; }
    public java.util.concurrent.CompletableFuture<HttpResponse<Object>> sendAsync(HttpRequest request, HttpResponse.BodyHandler<?> responseBodyHandler) { return java.util.concurrent.CompletableFuture.completedFuture(null); }
}