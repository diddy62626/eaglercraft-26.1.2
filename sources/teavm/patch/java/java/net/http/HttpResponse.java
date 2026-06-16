package java.net.http;
public interface HttpResponse<T> {
    int statusCode();
    HttpRequest request();
    HttpHeaders headers();
    T body();
    interface BodyHandler<T> {
        BodySubscriber<T> apply(ResponseInfo responseInfo);
    }
    interface BodySubscriber<T> {}
    interface ResponseInfo {
        int statusCode();
        HttpHeaders headers();
        HttpClient.Version version();
    }
}