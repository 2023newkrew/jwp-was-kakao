package requestmapper;

import controller.Controller;
import controller.UserController;
import request.Request;
import response.Response;

import java.util.List;

public final class HandlerMapper {

    private final List<Controller> controllers = List.of(UserController.getInstance());

    private static class LazyHolder {
        static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private HandlerMapper() {
    }

    public static HandlerMapper getInstance() {
        return HandlerMapper.LazyHolder.INSTANCE;
    }

    public Controller findHandler(String uri) {
        return controllers.stream()
                .filter(controller -> controller.canHandle(uri))
                .findFirst().orElse(null);
    }

    public Response handle(Request request, Controller handler) {
        return handler.handleRequest(request);
    }
}
