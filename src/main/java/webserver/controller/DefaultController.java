package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class DefaultController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        return HttpResponse.ok("Hello world".getBytes(), "text/html;charset=utf-8");
    }
}
