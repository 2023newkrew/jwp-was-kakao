package webserver.handler;

import webserver.request.HttpRequest;
import webserver.HttpResponse;

@FunctionalInterface
public interface Handler {
    HttpResponse applyRequest(HttpRequest request);
}
