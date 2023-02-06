package web.controller;

import http.Body;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatus;

import java.util.Objects;

public class DefaultController implements Controller {

    private static final String PATH = "/";

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        return HttpResponse.of(HttpStatus.OK, new Body("Hello world"));
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }
}
