package jcache;

import org.junit.Before;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

import static org.junit.Assert.assertEquals;

public class HelloWorldTest {

    private Cache<String, String> cache;

    @Before
    public void setup() {
        MutableConfiguration<String, String> cacheConfig = new MutableConfiguration<>();
        cacheConfig.setTypes(String.class, String.class);
        cache = Caching.getCachingProvider()
                       .getCacheManager()
                       .createCache("test", cacheConfig);
    }

    @Test
    public void testCache() {
        cache.put("hello", "world");
        assertEquals("world", cache.get("hello"));

        cache.replace("hello", "world!");
        assertEquals("world!", cache.get("hello"));
    }
}
