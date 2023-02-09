package framework.requestmapper;

import framework.controller.Controller;
import framework.request.Request;
import framework.response.Response;

import java.util.List;

public final class HandlerMapper {

    private List<Controller> controllers;

    private static class LazyHolder {
        static final HandlerMapper INSTANCE = new HandlerMapper();
    }

    private HandlerMapper() {
    }

    public static HandlerMapper getInstance() {
        return HandlerMapper.LazyHolder.INSTANCE;
    }

    public void setHandlers(List<Controller> controllers) {
        this.controllers = controllers;
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
