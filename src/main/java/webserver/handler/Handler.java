package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

@FunctionalInterface
public interface Handler {
    HttpResponse applyRequest(HttpRequest request);
}
