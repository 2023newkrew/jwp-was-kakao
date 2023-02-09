package webserver.handler.gethandler;

import webserver.constant.HttpMethod;
import webserver.handler.HttpRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public abstract class GetRequestHandler implements HttpRequestHandler {

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getTarget()
                .getMethod() == HttpMethod.GET;
    }

    @Override
    public abstract void handle(HttpRequest request, HttpResponse.Builder responseBuilder);
}
