package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.Objects;

public abstract class BaseController {
    abstract void processRequestByMethod(HttpRequest request, HttpResponse response);
    public void processRequest(HttpRequest request, HttpResponse response) {
        processRequestByMethod(request, response);
        
        if (Objects.nonNull(response.getHttpStatus())) return;
        response.setHttpStatus(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
