package webserver.handler.controller;

import org.springframework.http.HttpMethod;
import webserver.request.Request;
import webserver.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MethodHandlers {

    private final Map<HttpMethod, Map<String, Function<Request, Response>>> methodHandlers;

    public MethodHandlers() {
        this.methodHandlers = initHandlerMap();
    }

    private Map<HttpMethod, Map<String, Function<Request, Response>>> initHandlerMap() {
        return Map.of(
                HttpMethod.POST, new HashMap<>(),
                HttpMethod.GET, new HashMap<>(),
                HttpMethod.PATCH, new HashMap<>(),
                HttpMethod.DELETE, new HashMap<>()
        );
    }

    public void put(HttpMethod httpMethod, String uri, Function<Request, Response> handler) {
        Map<String, Function<Request, Response>> handlerMap = methodHandlers.get(httpMethod);
        handlerMap.put(uri, handler);
    }

    public boolean contains(HttpMethod httpMethod, String path) {
        Map<String, Function<Request, Response>> handlerMap = methodHandlers.get(httpMethod);

        return handlerMap.containsKey(path);
    }

    public Function<Request, Response> get(HttpMethod httpMethod, String path) {
        Map<String, Function<Request, Response>> handlerMap = methodHandlers.get(httpMethod);

        return handlerMap.get(path);
    }
}
