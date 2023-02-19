package webserver.handler;

import http.HttpRequest;
import http.HttpResponse;

public interface Handler {
    void handle(HttpRequest httpRequest, HttpResponse httpResponse);

    boolean support(HttpRequest httpRequest);
}
