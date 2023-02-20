package webserver.controller;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;
import webserver.utils.ResponseUtil;

import static webserver.request.HttpRequestMethod.GET;

public class HelloController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        ResponseUtil.response200(response, "Hello world".getBytes(), HttpResponseContentType.HTML);
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return request.getMethod() == GET && request.getUri().getPath().equals("/");
    }
}
