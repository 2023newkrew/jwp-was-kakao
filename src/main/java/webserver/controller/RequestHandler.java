package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

@FunctionalInterface
public interface RequestHandler {
    Response handle(Request request);
}
