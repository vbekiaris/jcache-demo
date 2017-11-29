package jcache.handler;

import com.sun.net.httpserver.HttpExchange;
import jcache.service.SlowService;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.net.URI;

import static jcache.service.SlowService.SERVICE;

/**
 * Cache-aside web request handler: if key not in cache, it is the handler's responsibility to request it from the producer
 * and put it in cache.
 */
public class CacheAsideHandler
        extends AbstractHandler {

    private final CacheManager cacheManager;
    private final Cache<URI, byte[]> cache;

    private SlowService service = SERVICE;

    public CacheAsideHandler(String context) {
        CachingProvider provider = Caching.getCachingProvider();
        this.cacheManager = provider.getCacheManager();
        Configuration<URI, byte[]> cacheConfig = new MutableConfiguration<URI, byte[]>()
                .setTypes(URI.class, byte[].class);
        this.cache = cacheManager.createCache(context, cacheConfig);
    }

    @Override
    public void handle(HttpExchange httpExchange)
            throws IOException {
        URI key = httpExchange.getRequestURI();
        byte[] responseBody = cache.get(key);
        if (responseBody != null) {
            respond(httpExchange, responseBody);
            return;
        }
        responseBody = service.greet(httpExchange.getRequestURI().toString());
        cache.put(key, responseBody);
        respond(httpExchange, responseBody);
    }
}
