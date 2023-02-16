package framework.requestmapper;

import framework.controller.ExceptionController;
import framework.response.Response;

import java.util.List;

public final class ExceptionHandlerMapper {

    private List<ExceptionController> controllers;

    private static class LazyHolder {
        static final ExceptionHandlerMapper INSTANCE = new ExceptionHandlerMapper();
    }

    private ExceptionHandlerMapper() {
    }

    public static ExceptionHandlerMapper getInstance() {
        return ExceptionHandlerMapper.LazyHolder.INSTANCE;
    }

    public void setHandlers(List<ExceptionController> controllers) {
        this.controllers = controllers;
    }

    public ExceptionController findHandler(Throwable throwable) {
        return controllers.stream()
                .filter(controller -> controller.canHandle(throwable))
                .findFirst().orElse(null);
    }

    public Response handle(Throwable throwable, ExceptionController handler) {
        return handler.handleRequest(throwable);
    }
}
