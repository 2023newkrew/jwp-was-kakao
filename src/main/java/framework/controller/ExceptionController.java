package framework.controller;

import framework.response.Response;

public interface ExceptionController {

    Response handleRequest(Throwable throwable);

    boolean canHandle(Throwable throwable);
}
