package controller;

import support.MethodNotAllowedException;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Objects;

public abstract class BaseController {
    abstract void processRequestByMethod(HttpRequest request, HttpResponse response);
    public void handleRequest(HttpRequest request, HttpResponse response) {
        processRequestByMethod(request, response);
        
        if (Objects.isNull(response.getHttpStatus())) {
            throw new MethodNotAllowedException();
        }
    }
}
