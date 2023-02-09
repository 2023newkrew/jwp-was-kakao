package web.controller;

import http.*;

import java.util.Objects;

public class DefaultController implements Controller {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        return HttpResponse.of(HttpStatus.OK, new Body("Hello world"));
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals("/", httpRequest.getPath());
    }
}
