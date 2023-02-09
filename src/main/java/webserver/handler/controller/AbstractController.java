package webserver.handler.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import webserver.handler.Handler;
import webserver.handler.resolver.Resolver;
import webserver.http.header.Headers;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.ResponseBody;
import webserver.response.ResponseHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractController implements Handler {

    private final Map<MethodPath, RequestHandler> requestHandlers = new HashMap<>();

    private final Resolver viewResolver;

    public AbstractController(Resolver viewResolver) {
        this.viewResolver = viewResolver;
    }

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

    protected ResponseBody resolve(String path) {
        return viewResolver.resolve(path);
    }

    protected Response createResponse(HttpStatus httpStatus) {
        return createResponse(httpStatus, null, null);
    }

    protected Response createResponse(HttpStatus httpStatus, ResponseBody body) {
        return createResponse(httpStatus, null, body);
    }

    protected Response createResponse(HttpStatus httpStatus, Headers headers) {
        return createResponse(httpStatus, headers, null);
    }

    protected Response createResponse(HttpStatus httpStatus, Headers headers, ResponseBody body) {
        ResponseHeader header = new ResponseHeader(httpStatus, headers, body);

        return new Response(header, body);
    }
}
