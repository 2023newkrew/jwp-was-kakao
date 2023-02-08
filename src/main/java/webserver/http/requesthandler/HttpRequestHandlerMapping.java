package webserver.http.requesthandler;

import webserver.exceptions.HandlerNotFoundException;
import webserver.http.request.HttpRequest;

public interface HttpRequestHandlerMapping {
    HttpRequestHandler findHandler(HttpRequest request) throws HandlerNotFoundException;
}
