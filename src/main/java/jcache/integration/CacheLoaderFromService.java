package jcache.integration;

import jcache.service.SlowService;

import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static jcache.service.SlowService.SERVICE;

public class CacheLoaderFromService implements CacheLoader<URI, byte[]> {

    private SlowService service = SERVICE;

    @Override
    public byte[] load(URI key)
            throws CacheLoaderException {
        try {
            return service.greet(key.toString());
        } catch (IOException e) {
            throw new CacheLoaderException(e);
        }
    }

    @Override
    public Map<URI, byte[]> loadAll(Iterable<? extends URI> keys)
            throws CacheLoaderException {
        Map<URI, byte[]> result = new HashMap<>();
        try {
            for (URI k : keys) {
                result.put(k, service.greet(k.toString()));
            }
            return result;
        } catch (Exception e) {
            throw new CacheLoaderException(e);
        }
    }
}
