package web.controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

import java.util.Objects;

public class GetResourceController implements Controller {

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        return HttpResponse.of(HttpStatus.OK, getSuffix(httpRequest) + httpRequest.getPath());
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod());
    }

    private String getSuffix(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.contains(".html")) {
            return "templates";
        }

        return "static";
    }

}
