package webserver.controller;

import java.util.Map;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class PostController implements Controller {
    @Override
    public boolean isHandleable(HttpRequest request) {
        return request.getMethod().equals("POST") && request.getPath().equals("/post");
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Map<String, String> applicationForm = request.toApplicationForm();
        return HttpResponse.ok(String.format("hello %s", applicationForm.get("name")).getBytes());
    }
}
