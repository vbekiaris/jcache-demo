package jcache.handler;

import com.sun.net.httpserver.HttpExchange;
import jcache.integration.CacheLoaderFromService;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.net.URI;

/**
 * Cache-through web request handler: if key is not in cache, the configured {@code CacheLoader} attempts to obtain the
 * requested data from the producer.
 */
public class CacheThroughHandler extends AbstractHandler {

    private final CacheManager cacheManager;
    private final Cache<URI, byte[]> cache;

    public CacheThroughHandler(String context) {
        CachingProvider provider = Caching.getCachingProvider();
        this.cacheManager = provider.getCacheManager();
        MutableConfiguration<URI, byte[]> cacheConfig = new MutableConfiguration<URI, byte[]>()
                .setTypes(URI.class, byte[].class);
        cacheConfig.setReadThrough(true)
                   .setCacheLoaderFactory(() -> {
                        return new CacheLoaderFromService();
                   });
        this.cache = cacheManager.createCache(context, cacheConfig);
    }

    @Override
    public void handle(HttpExchange httpExchange)
            throws IOException {
        URI key = httpExchange.getRequestURI();
        byte[] responseBody = cache.get(key);
        respond(httpExchange, responseBody);
        return;
    }
}
