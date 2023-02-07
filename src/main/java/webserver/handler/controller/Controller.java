package webserver.handler.controller;

import webserver.handler.Handler;
import webserver.request.Request;
import webserver.response.Response;

import java.util.function.Function;

public abstract class Controller implements Handler {

    private final String prefix;

    protected final MethodHandlers methodHandlers;

    public Controller(String prefix) {
        this.prefix = prefix;
        this.methodHandlers = new MethodHandlers();
    }

    @Override
    public boolean canHandle(Request request) {
        return methodHandlers.contains(request.getHttpMethod(), getFullPath(request));
    }

    private String getFullPath(Request request) {
        return prefix + request.getPath();
    }

    @Override
    public Response handle(Request request) {
        Function<Request, Response> handler = methodHandlers.get(request.getHttpMethod(), getFullPath(request));

        return handler.apply(request);
    }
}
