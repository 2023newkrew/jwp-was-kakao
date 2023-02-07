package controller.controllers;

import controller.annotation.CustomRequestMapping;
import http.request.CustomHttpMethod;
import http.request.CustomHttpRequest;
import http.response.CustomHttpResponse;
import http.response.CustomHttpStatus;
import utils.FileIoUtils;

import java.util.HashMap;
import java.util.Map;

public class ViewController extends BaseController {

    @CustomRequestMapping(url = "/", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse main(CustomHttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", "11");
        return new CustomHttpResponse("HTTP/1.1", CustomHttpStatus.OK, headers, "Hello world");
    }

    @CustomRequestMapping(url = "/index.html", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse index(CustomHttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", "6902");

        return new CustomHttpResponse(
                "HTTP/1.1",
                CustomHttpStatus.OK,
                headers,
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html")));
    }

    @CustomRequestMapping(url = "/css/styles.css", httpMethod = CustomHttpMethod.GET)
    public CustomHttpResponse css(CustomHttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/css;charset=utf-8");
        headers.put("Content-Length", "7065");
        return new CustomHttpResponse(
                "HTTP/1.1",
                CustomHttpStatus.OK,
                headers,
                new String(FileIoUtils.loadFileFromClasspath("static/css/styles.css")));
    }

}
