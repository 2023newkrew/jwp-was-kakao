package webserver;

import http.request.HttpMethod;
import webserver.controller.Controller;
import webserver.controller.UserCreateController;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private static final Map<HttpMethod, Map<String, Controller>> ROUTING_MAP = new HashMap<>();

    static {
        ROUTING_MAP.put(HttpMethod.GET, new HashMap<>());
        ROUTING_MAP.put(HttpMethod.POST, new HashMap<>());
        ROUTING_MAP.put(HttpMethod.PUT, new HashMap<>());
        ROUTING_MAP.put(HttpMethod.DELETE, new HashMap<>());

        ROUTING_MAP.get(HttpMethod.POST).put("/user/create", new UserCreateController());
    }

    public static Controller getController(HttpMethod method, String path) {
        var methodRouters = ROUTING_MAP.getOrDefault(method, null);
        if (methodRouters == null) {
            return null;
        }
        return methodRouters.getOrDefault(path, null);
    }
}
