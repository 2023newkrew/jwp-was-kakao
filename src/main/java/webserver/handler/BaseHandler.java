package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class BaseHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        return HttpResponse.ok("Hello world".getBytes(), request.getFilenameExtension());
    }
}
