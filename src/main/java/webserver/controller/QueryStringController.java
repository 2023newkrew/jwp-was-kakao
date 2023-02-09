package webserver.controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public class QueryStringController implements Controller {
    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getPath().equals("/query");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String body = "hello " + request.getParameter("name");
        return HttpResponse.ok(body.getBytes());
    }
}
