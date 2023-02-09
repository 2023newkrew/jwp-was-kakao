package webserver.controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public class HelloWorldController implements Controller {
    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getPath().equals("/");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return HttpResponse.ok("Hello world".getBytes());
    }
}
