package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseStatus;

public interface Controller {

    void service(HttpRequest request, HttpResponse response);

    boolean isMatch(HttpRequest request);

    HttpResponseStatus getSuccessCode();
}
