package web.controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Controller {

    HttpResponse run(HttpRequest httpRequest);
    boolean isMatch(HttpRequest httpRequest);

}
