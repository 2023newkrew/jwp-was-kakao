package webserver.handler.controller;

import webserver.handler.Handler;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Objects;
import java.util.function.Function;

public class Controller implements Handler {

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
