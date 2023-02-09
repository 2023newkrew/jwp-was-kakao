package webserver.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class StaticResourceController implements Controller {

    @Override
    public boolean isHandleable(HttpRequest request) {
        return true;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        try {
            if (request.getPath().endsWith(".html")) {
                byte[] body = FileIoUtils.loadFileFromClasspath("templates" + request.getPath());
                return HttpResponse.ok(body);
            }
            return HttpResponse.ok(FileIoUtils.loadFileFromClasspath("static" + request.getPath()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
