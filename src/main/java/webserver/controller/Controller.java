package webserver.controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {

    boolean isHandleable(HttpRequest request);

    HttpResponse handle(HttpRequest request);
}
