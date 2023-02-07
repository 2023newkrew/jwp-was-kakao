package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface HttpRequestHandler {

    boolean canHandle(HttpRequest request);

    HttpResponse handle(HttpRequest request);
}
