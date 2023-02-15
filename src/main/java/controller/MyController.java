package controller;

import webserver.request.HttpRequest;
import webserver.response.ResponseEntity;

public interface MyController {

    boolean canHandle(HttpRequest httpRequest);

    ResponseEntity handle(HttpRequest httpRequest);
}
