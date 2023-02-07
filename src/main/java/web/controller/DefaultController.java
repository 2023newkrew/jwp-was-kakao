package web.controller;

import http.Body;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

import java.util.Objects;

import static http.ContentType.TEXT_PLAIN;
import static http.HttpHeaders.CONTENT_LENGTH;
import static http.HttpHeaders.CONTENT_TYPE;

public class DefaultController implements Controller {

    private static final String PATH = "/";

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        return HttpResponse.ok(
                () -> new Body("Hello world"),
                (body) -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.put(CONTENT_TYPE, TEXT_PLAIN.toString());
                    httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));
                    return httpHeaders;
                });
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod()) && Objects.equals(PATH, httpRequest.getPath());
    }
}
