package jcache.handler;

import com.sun.net.httpserver.HttpExchange;
import jcache.service.SlowService;

import java.io.IOException;

import static jcache.service.SlowService.SERVICE;

public class HelloWorldHandler extends AbstractHandler {

    private SlowService service = SERVICE;

    @Override
    public void handle(HttpExchange httpExchange)
            throws IOException {
        byte[] response = service.greet(httpExchange.getRequestURI().toString());
        respond(httpExchange, response);
    }
}
