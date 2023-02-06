package webserver.controller;

import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;

public abstract class GetController implements Controller {
    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        HttpMethod method = httpRequest.getMethod();
        if (!method.equals(HttpMethod.GET)) {
            throw new RuntimeException();
        }
        doGet(httpRequest, httpResponse);
    }

    protected abstract void doGet(HttpRequest httpRequest, HttpResponse httpResponse);
}
