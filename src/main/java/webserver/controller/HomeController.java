package webserver.controller;

import http.HttpResponse;
import http.request.HttpRequest;

public class HomeController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) {
        return "/index.html";
    }
}
