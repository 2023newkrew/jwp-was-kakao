package webserver.controller;

import http.HttpResponse;
import http.request.HttpRequest;

public class StaticResourceController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        return request.getPath();
    }
}
