package requestmapper;

import controller.Controller;
import controller.MainController;
import request.Request;
import response.Response;

import java.util.HashMap;
import java.util.Map;

public final class HandlerMapper {

    private final Map<String, Controller> map = new HashMap<>();

    private static class LazyHolder {
        static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private HandlerMapper() {
        map.put("/", MainController.getInstance());
        map.put("/user/create", MainController.getInstance());
    }

    public static HandlerMapper getInstance() {
        return HandlerMapper.LazyHolder.INSTANCE;
    }

    public boolean isHandleAvailable(String uri) {
        return map.containsKey(uri);
    }

    public Response handle(Request request) {
        return map.get(request.getUri()).handleRequest(request);
    }
}
