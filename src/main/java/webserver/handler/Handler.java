package webserver.handler;

import http.HttpRequest;
import http.HttpResponse;

public interface Handler {
    public HttpResponse handle(HttpRequest httpRequest);
}
