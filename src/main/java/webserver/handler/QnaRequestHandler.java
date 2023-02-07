package webserver.handler;

import http.*;
import utils.FileIoUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QnaRequestHandler implements UrlMappingHandler {

    private static final String URL_MAPPING_REGEX = "/qna/.+";
    private static final String CHARSET_UTF_8 = "charset=utf-8";
    private static final String TEMPLATES_FILEPATH = "./templates";

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath(TEMPLATES_FILEPATH + httpRequest.getURL());
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

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }

    private Map<String, List<String>> generateHeaders(HttpRequest httpRequest, byte[] body) {
        Map<String, List<String>> headers = new LinkedHashMap<>();

        String url = httpRequest.getURL();
        int extensionIndex = url.lastIndexOf(".") + 1;
        String extension = url.substring(extensionIndex);

        headers.put(HttpHeader.CONTENT_TYPE,
                List.of(HttpContentType.extensionToContentType(extension) + ";" + CHARSET_UTF_8));

        headers.put(HttpHeader.CONTENT_LENGTH, List.of(String.valueOf(body.length)));

        return headers;
    }
}
