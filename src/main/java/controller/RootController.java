package controller;

import common.HttpMethod;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;

public class RootController implements Controller {
    private static final String DEFAULT_RESPONSE = "Hello world";

    public void process(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(DEFAULT_RESPONSE.getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
    }
}
