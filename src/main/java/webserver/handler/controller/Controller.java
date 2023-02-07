package webserver.handler.controller;

import webserver.handler.Handler;
import webserver.http.request.Request;
import webserver.http.response.Response;

import java.util.Objects;
import java.util.function.Function;

public abstract class Controller implements Handler {

    protected final MethodHandlers methodHandlers = new MethodHandlers();

    @Override
    public boolean canHandle(Request request) {
        return methodHandlers.contains(request.getHttpMethod(), request.getPath());
    }


    @Override
    public Response handle(Request request) {
        Function<Request, Response> handler = methodHandlers.get(request.getHttpMethod(), request.getPath());
        if (Objects.isNull(handler)) {
            throw new RuntimeException();
        }

        return handler.apply(request);
    }
}
