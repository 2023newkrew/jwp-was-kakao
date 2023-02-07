package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

@FunctionalInterface
public interface ControllerMethod {
    void handle(Request request, Response response);
}
