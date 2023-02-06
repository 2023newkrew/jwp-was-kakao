package controller;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

public class RootController extends BaseController {
    private static final String DEFAULT_RESPONSE = "Hello world";

    @Override
    void processRequestByMethod(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(DEFAULT_RESPONSE.getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
    }
}
