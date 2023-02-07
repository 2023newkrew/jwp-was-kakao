package requestmapper;

import controller.Controller;
import controller.MainController;
import request.Request;
import response.Response;

import java.util.HashMap;
import java.util.Map;

public final class HandlerMapper {

    public static final HandlerMapper INSTANCE = new HandlerMapper();

    private final Map<String, Controller> map = new HashMap<>();

    private HandlerMapper() {
        map.put("/", MainController.INSTANCE);
        map.put("/user/create", MainController.INSTANCE);
    }

    public boolean isHandleAvailable(String uri) {
        return map.containsKey(uri);
    }

    public Response handle(Request request) {
        return map.get(request.getUri()).handleRequest(request);
    }
}
