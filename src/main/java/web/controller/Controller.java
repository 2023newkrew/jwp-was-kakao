package web.controller;

import http.HttpRequest;
import http.HttpResponse;

public interface Controller {

    HttpResponse run(HttpRequest httpRequest);
    boolean isMatch(HttpRequest httpRequest);

}
