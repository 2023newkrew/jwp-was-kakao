package controller;

import common.HttpMethod;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;

public class RootController extends Controller {
    private static final String DEFAULT_RESPONSE = "Hello world";

    @Override
    void handleRequestByMethod(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals(HttpMethod.GET)) {
            response.setBody(DEFAULT_RESPONSE.getBytes());
            response.setHttpStatus(HttpStatus.OK);
        }
    }
}
