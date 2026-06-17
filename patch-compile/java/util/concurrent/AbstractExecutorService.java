package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TeaVM-compatible stub for AbstractExecutorService.
 */
public abstract class AbstractExecutorService implements ExecutorService {
    @Override public <T> Future<T> submit(Callable<T> task) {
        try {
            T result = task.call();
            CompletableFuture<T> f = new CompletableFuture<>();
            f.complete(result);
            return f;
        } catch (Exception e) {
            CompletableFuture<T> f = new CompletableFuture<>();
            f.completeExceptionally(e);
            return f;
        }
    }

    @Override public Future<?> submit(Runnable task) {
        task.run();
        return CompletableFuture.completedFuture(null);
    }

    @Override public <T> Future<T> submit(Runnable task, T result) {
        task.run();
        return CompletableFuture.completedFuture(result);
    }

    @Override public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        List<Future<T>> results = new ArrayList<>();
        for (Callable<T> task : tasks) {
            results.add(submit(task));
        }
        return results;
    }

    @Override public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        for (Callable<T> task : tasks) {
            try { return task.call(); } catch (Exception e) { continue; }
        }
        throw new RuntimeException("No task completed successfully");
    }
}
