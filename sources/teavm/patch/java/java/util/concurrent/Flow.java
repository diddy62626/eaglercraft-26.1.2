package java.util.concurrent;
public final class Flow {
    public interface Publisher<T> { void subscribe(Subscriber<? super T> subscriber); }
    public interface Subscriber<T> { void onSubscribe(Subscription s); void onNext(T t); void onError(Throwable t); void onComplete(); }
    public interface Subscription { void request(long n); void cancel(); }
    public interface Processor<T,R> extends Subscriber<T>, Publisher<R> {}
}