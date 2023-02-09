package webserver.handler.posthandler;

import webserver.constant.HttpMethod;
import webserver.handler.HttpRequestHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public abstract class PostRequestHandler implements HttpRequestHandler {

    @Override
    public boolean canHandle(HttpRequest request) {
        return request.getTarget()
                .getMethod() == HttpMethod.POST;
    }

    @Override
    public abstract void handle(HttpRequest request, HttpResponse.Builder responseBuilder);
}
