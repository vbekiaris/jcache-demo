package jcache;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Caching;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static jcache.service.SlowService.SERVICE;

public class HelloWorldAppTest {

    private HelloWorldApp app;
    private OkHttpClient httpClient;

    @Before
    public void setUp()
            throws Exception {
        app = new HelloWorldApp();
        app.start();
        httpClient = new OkHttpClient();
    }

    @After
    public void tearDown() {
        app.stop();
        Caching.getCachingProvider().close();
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    @Test
    public void testConcurrentRequests()
            throws InterruptedException {
        int concurrency = 4;
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(concurrency);
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < concurrency; i++) {
            concurrencyHelper.submit(() -> {
                try {
                    latch.await();
                    System.out.println(Thread.currentThread() + " First request, time taken: " + millisTakenToGet());
                    System.out.println(Thread.currentThread() + " Second request, time taken: " + millisTakenToGet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        latch.countDown();
        concurrencyHelper.awaitCompletion();
        System.out.println("Service was invoked " + SERVICE.getInvocationCount() + " times");
    }

    private long millisTakenToGet() throws IOException {
        long start = System.nanoTime();
        Request request = new Request.Builder().url("http://localhost:8080/hello").build();
        Response response = httpClient.newCall(request).execute();
        long duration = System.nanoTime() - start;
        response.close();
        return duration / 1_000_000;
    }
}
