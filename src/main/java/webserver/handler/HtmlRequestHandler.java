package webserver.handler;

import utils.FileIoUtils;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

public class HtmlRequestHandler implements Handler {
    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath("./templates" + httpRequest.getURL());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.OK)
                .withVersion("HTTP/1.1")
                .withHeaders(generateHeaders(httpRequest, bytes))
                .withBody(bytes)
                .build();
    }

    private Map<String, String> generateHeaders(HttpRequest httpRequest, byte[] body) {
        Map<String, String> headers = new LinkedHashMap<>();

        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.length));

        return headers;
    }
}
