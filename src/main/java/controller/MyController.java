package controller;

import webserver.request.HttpRequest;
import webserver.request.MyHeaders;
import webserver.request.MyParams;
import webserver.response.ResponseEntity;

import java.io.DataOutputStream;

public interface MyController {

    boolean canHandle(HttpRequest httpRequest);

    ResponseEntity handle(HttpRequest httpRequest, DataOutputStream dataOutputStream);
}
