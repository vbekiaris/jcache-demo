package jcache;

import com.sun.net.httpserver.HttpServer;
import jcache.handler.CacheAsideHandler;
import jcache.handler.CacheThroughHandler;
import jcache.handler.HelloWorldHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HelloWorldApp {

    private HttpServer server;

    void start() {
        try {
            InetSocketAddress serverSocket = new InetSocketAddress(8080);
            server = HttpServer.create(serverSocket, 0);
            // an executor with 4 threads will serve requests
            server.setExecutor(Executors.newFixedThreadPool(4));
            // uncomment one of three context creation statements below to  see each handler in action
            // no caching
//             server.createContext("/hello", new HelloWorldHandler());
            // cache-aside handler
//            server.createContext("/hello", new CacheAsideHandler("hello"));
            // cache-through handler
             server.createContext("/hello", new CacheThroughHandler("hello"));
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void stop() {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        HelloWorldApp app = new HelloWorldApp();
        app.start();
    }
}
