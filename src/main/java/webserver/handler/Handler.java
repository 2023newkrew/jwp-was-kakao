package webserver.handler;

import http.HttpRequest;
import http.HttpResponse;

public interface Handler {
    HttpResponse handle(HttpRequest httpRequest);

    boolean support(HttpRequest httpRequest);
}
