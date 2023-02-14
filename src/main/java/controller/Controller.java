package controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface Controller {
    HttpResponse response(HttpRequest httpRequest);
}
