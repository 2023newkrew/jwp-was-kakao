package webserver.constants.method;

import webserver.handler.request.GetRequestHandler;
import webserver.handler.request.PostRequestHandler;
import webserver.handler.request.RequestMethodHandler;

public enum HttpMethod {
    GET(GetRequestHandler.getInstance()),
    POST(PostRequestHandler.getInstance());

    private final RequestMethodHandler requestMethodHandler;

    HttpMethod(RequestMethodHandler requestMethodHandler) {
        this.requestMethodHandler = requestMethodHandler;
    }

    public RequestMethodHandler getRequestMethodHandler() {
        return requestMethodHandler;
    }
}
