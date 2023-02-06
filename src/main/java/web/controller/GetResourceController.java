package web.controller;

import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatus;

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
