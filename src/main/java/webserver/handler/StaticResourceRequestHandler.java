package webserver.handler;

import utils.FileIoUtils;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticResourceRequestHandler implements Handler {

    public static final String STATIC_FILEPATH = "./static";

    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath(STATIC_FILEPATH + httpRequest.getURL());
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

    private Map<String, List<String>> generateHeaders(HttpRequest httpRequest, byte[] body) {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        String url = httpRequest.getURL();
        if (url.endsWith(".js")) {
            headers.put("Content-Type", List.of("text/javascript;charset=utf-8"));
        } else if (url.endsWith(".css")) {
            headers.put("Content-Type", List.of("text/css;charset=utf-8"));
        } else {
            headers.put("Content-Type", List.of("text/html;charset=utf-8"));
        }

        headers.put("Content-Length", List.of(String.valueOf(body.length)));

        return headers;
    }
}
