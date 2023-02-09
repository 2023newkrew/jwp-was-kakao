package webserver.handler.controller;

import org.springframework.http.HttpMethod;
import webserver.handler.Handler;
import webserver.request.Request;
import webserver.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractController implements Handler {

    protected final Map<MethodPath, RequestHandler> requestHandlers = new HashMap<>();

    @Override
    public boolean canHandle(Request request) {
        return requestHandlers.containsKey(new MethodPath(request));
    }

    @Override
    public Response handle(Request request) {
        RequestHandler handler = requestHandlers.get(new MethodPath(request));
        if (Objects.isNull(handler)) {
            throw new RuntimeException();
        }

        return handler.handle(request);
    }

    protected void addGetHandler(String path, RequestHandler requestHandler) {
        MethodPath key = new MethodPath(HttpMethod.GET, path);
        addRequestHandler(key, requestHandler);
    }

    protected void addPostHandler(String path, RequestHandler requestHandler) {
        MethodPath key = new MethodPath(HttpMethod.POST, path);
        addRequestHandler(key, requestHandler);
    }

    protected void addPatchHandler(String path, RequestHandler requestHandler) {
        MethodPath key = new MethodPath(HttpMethod.PATCH, path);
        addRequestHandler(key, requestHandler);
    }

    protected void addDeleteHandler(String path, RequestHandler requestHandler) {
        MethodPath key = new MethodPath(HttpMethod.DELETE, path);
        addRequestHandler(key, requestHandler);
    }

    private void addRequestHandler(MethodPath methodPath, RequestHandler requestHandler) {
        requestHandlers.put(methodPath, requestHandler);
    }
}
