package webserver.handler;

import webserver.FilenameExtension;
import webserver.request.HttpRequest;
import webserver.HttpResponse;

public class BaseHandler implements Handler {

    @Override
    public HttpResponse applyRequest(HttpRequest request) {
        return HttpResponse.ok("Hello world".getBytes(), FilenameExtension.from(""));
    }
}
