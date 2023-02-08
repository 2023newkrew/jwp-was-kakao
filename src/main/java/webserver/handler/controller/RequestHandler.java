package webserver.handler.controller;

import webserver.http.request.Request;
import webserver.http.response.Response;

@FunctionalInterface
public interface RequestHandler {
    Response handle(Request request);
}
