package jcache;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A small helper that supplies facilities to execute the same {@link Runnable} concurrently on
 * a number of threads and wait for all executions to complete.
 */
public class ConcurrencyHelper {

    private final int concurrency;
    private CompletionService<Void> completionService;

    public ConcurrencyHelper(int concurrency) {
        this.concurrency = concurrency;
        ExecutorService executorService = Executors.newFixedThreadPool(concurrency);
        completionService = new ExecutorCompletionService<>(executorService);
    }

    public void submit(Runnable runnable) {
        for (int i = 0; i < concurrency; i++) {
            completionService.submit(runnable, null);
        }
    }

    public void awaitCompletion() {
        for (int i = 0; i < concurrency; i++) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
