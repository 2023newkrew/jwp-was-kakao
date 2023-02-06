package webserver.controller;

import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

public abstract class PostController implements Controller {
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getMethod();
        if (!method.equals(HttpMethod.POST)) {
            throw new RuntimeException();
        }

    }

    protected abstract void doPost(HttpRequest httpRequest, HttpResponse httpResponse);
}
