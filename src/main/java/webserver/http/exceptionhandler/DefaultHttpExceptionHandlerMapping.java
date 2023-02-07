package webserver.http.exceptionhandler;

import webserver.exceptions.HandlerNotFoundException;

public class DefaultHttpExceptionHandlerMapping implements HttpExceptionHandlerMapping {
    @Override
    public HttpExceptionHandler findHandler(Exception e) throws HandlerNotFoundException {
        throw new HandlerNotFoundException();
    }
}
