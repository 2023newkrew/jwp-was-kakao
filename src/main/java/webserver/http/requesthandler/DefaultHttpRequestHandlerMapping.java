package webserver.http.requesthandler;

import webserver.exceptions.HandlerNotFoundException;
import webserver.http.request.HttpRequest;

public class DefaultHttpRequestHandlerMapping implements HttpRequestHandlerMapping {
    @Override
    public HttpRequestHandler findHandler(HttpRequest request) throws HandlerNotFoundException {
        throw new HandlerNotFoundException();
    }
}
