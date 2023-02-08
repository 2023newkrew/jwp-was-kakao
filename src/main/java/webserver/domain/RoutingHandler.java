package webserver.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RoutingHandler {
    private static final Map<HttpEndpoint, Consumer<Context>> handlerMapper = new ConcurrentHashMap<>();

    public void addHandler(HttpMethod method, String relativePath, Consumer<Context> handler) {
        handlerMapper.put(new HttpEndpoint(method, relativePath), handler);
    }

    public boolean canHandle(HttpMethod method, String relativePath) {
        return handlerMapper.containsKey(new HttpEndpoint(method, relativePath));
    }

    public Consumer<Context> getHandler(HttpMethod method, String relativePath) {
        return handlerMapper.get(new HttpEndpoint(method, relativePath));
    }
}
