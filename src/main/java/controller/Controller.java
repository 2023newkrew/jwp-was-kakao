package controller;

import support.MethodNotAllowedException;
import common.HttpRequest;
import common.HttpResponse;

import java.util.Objects;

public abstract class Controller {
    abstract void handleRequestByMethod(HttpRequest request, HttpResponse response);
    public void handleRequest(HttpRequest request, HttpResponse response) {
        handleRequestByMethod(request, response);
        
        if (Objects.isNull(response.getHttpStatus())) {
            throw new MethodNotAllowedException();
        }
    }
}
