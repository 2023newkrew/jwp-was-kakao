package webserver.http.requesthandler;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

@FunctionalInterface
public interface HttpRequestHandler {
    HttpResponse doHandle(HttpRequest request) throws Exception;
}
