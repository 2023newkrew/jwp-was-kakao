package webserver.router;

import http.request.HttpMethod;
import webserver.controller.Controller;
import webserver.controller.UserCreateController;
import webserver.controller.UserListController;
import webserver.controller.UserLoginController;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private static final Map<Route, Controller> ROUTING_MAP = new HashMap<>();

    static {
        ROUTING_MAP.put(Route.of(HttpMethod.POST, "/user/create"), new UserCreateController());
        ROUTING_MAP.put(Route.of(HttpMethod.POST, "/user/login"), new UserLoginController());
        ROUTING_MAP.put(Route.of(HttpMethod.GET, "/user/list"), new UserListController());
    }

    public static Controller getController(HttpMethod method, String path) {
        return ROUTING_MAP.get(Route.of(method, path));
    }
}
