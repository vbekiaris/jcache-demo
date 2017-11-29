package jcache.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

public enum SlowService {

    SERVICE;

    public static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String GREETING_TEMPLATE = "Hello, %s";

    private AtomicInteger invocationCounter = new AtomicInteger();

    public byte[] greet(String peer)
            throws IOException {
        invocationCounter.getAndIncrement();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String response = format(GREETING_TEMPLATE, peer);
        byte[] responseBytes = response.getBytes(UTF8);
        return responseBytes;
    }

    public int getInvocationCount() {
        return invocationCounter.get();
    }
}
