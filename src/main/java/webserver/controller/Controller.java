package webserver.controller;

import http.HttpResponse;
import http.request.HttpRequest;

public interface Controller {

    String process(HttpRequest request, HttpResponse response);
}
