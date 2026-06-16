package java.util.concurrent;
public class ForkJoinPool {
    public interface ForkJoinWorkerThreadFactory {
        ForkJoinWorkerThread newThread(ForkJoinPool pool);
    }
}
