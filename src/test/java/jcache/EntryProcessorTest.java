package jcache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.processor.EntryProcessor;
import javax.cache.spi.CachingProvider;
import java.util.concurrent.CountDownLatch;

public class EntryProcessorTest {

    CachingProvider cachingProvider;
    CacheManager cacheManager;
    Cache<Integer, Integer> salaries;

    @Before
    public void setup() {
        cachingProvider = Caching.getCachingProvider();
        cacheManager = cachingProvider.getCacheManager();
        salaries = cacheManager.getCache("salaries");
        salaries.put(1, 1000);
    }

    // Two managers want to increase the salary of employee with ID 1 by 100 at the same time.
    // The get-update-put approach in this test is racy and results in lost update.
    @Test
    public void testIncreaseSalary_withoutEP() {
        CountDownLatch latch = new CountDownLatch(1);
        int concurrency = 2;
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(concurrency);
        concurrencyHelper.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int salary = salaries.get(1);
            salary += 100;
            salaries.put(1, salary);
        });
        latch.countDown();
        concurrencyHelper.awaitCompletion();
        Assert.assertEquals(1200L, (long) salaries.get(1));
    }

    // Same as above, this time properly implemented with an {@link EntryProcessor}, which
    // is guaranteed to be executed atomically under JCache's default consistency model.
    @Test
    public void testIncreaseSalary_withEP() {
        CountDownLatch latch = new CountDownLatch(1);
        int concurrency = 2;
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(concurrency);
        concurrencyHelper.submit(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            EntryProcessor<Integer, Integer, Integer> increaseSalaryEP = (entry, arguments) -> {
                int currentSalary = entry.getValue();
                int increasedSalary = currentSalary + (int) arguments[0];
                entry.setValue(increasedSalary);
                return increasedSalary;
            };
            salaries.invoke(1, increaseSalaryEP, 100);
        });
        latch.countDown();
        concurrencyHelper.awaitCompletion();
        Assert.assertEquals(1200L, (long) salaries.get(1));
    }
}
