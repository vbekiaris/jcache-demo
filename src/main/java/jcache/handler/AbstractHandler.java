package jcache.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Base class for web request handlers
 */
public abstract class AbstractHandler implements HttpHandler {

    /**
     * Send {@code responseBytes} with status 200 and content type {@code text/plain}
     */
    void respond(HttpExchange httpExchange, byte[] responseBytes)
            throws IOException {
        httpExchange.getResponseHeaders().add("Content-type", "text/plain;charset=utf-8");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        httpExchange.getResponseBody().write(responseBytes);
        httpExchange.getResponseBody().close();
    }
}
