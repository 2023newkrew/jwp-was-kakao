package webserver.http.exceptionhandler;

import webserver.exceptions.HandlerNotFoundException;

public interface HttpExceptionHandlerMapping {
    HttpExceptionHandler findHandler(Exception e) throws HandlerNotFoundException;
}
