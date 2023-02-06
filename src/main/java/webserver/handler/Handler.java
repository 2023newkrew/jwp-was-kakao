package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface Handler {
    void service(HttpRequest request, HttpResponse response);
}
