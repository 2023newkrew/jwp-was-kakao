package webserver.http.exceptionhandler;

import webserver.http.response.HttpResponse;

@FunctionalInterface
public interface HttpExceptionHandler {
    HttpResponse doHandle(Exception e);
}
