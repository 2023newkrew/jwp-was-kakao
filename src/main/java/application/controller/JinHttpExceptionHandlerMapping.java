package application.controller;

import webserver.exceptions.HandlerNotFoundException;
import webserver.http.exceptionhandler.HttpExceptionHandler;
import webserver.http.exceptionhandler.HttpExceptionHandlerMapping;

public class JinHttpExceptionHandlerMapping implements HttpExceptionHandlerMapping {
    @Override
    public HttpExceptionHandler findHandler(Exception e) throws HandlerNotFoundException {
        throw new HandlerNotFoundException();
    }
}
