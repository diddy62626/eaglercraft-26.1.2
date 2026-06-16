package java.net.http;
public class HttpRequest {
    public static class Builder {
        public Builder uri(java.net.URI uri) { return this; }
        public Builder header(String name, String value) { return this; }
        public Builder GET() { return this; }
        public Builder POST(BodyPublisher bodyPublisher) { return this; }
        public Builder PUT(BodyPublisher bodyPublisher) { return this; }
        public HttpRequest build() { return new HttpRequest(); }
    }
    public static Builder newBuilder() { return new Builder(); }
    public static Builder newBuilder(java.net.URI uri) { return new Builder().uri(uri); }
}