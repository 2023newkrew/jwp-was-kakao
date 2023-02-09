package webserver.handler;

import http.*;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StaticResourceRequestHandler implements Handler {

    public static final String STATIC_FILEPATH = "./static";
    public static final String CHARSET_UTF_8 = "charset=utf-8";

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath(STATIC_FILEPATH + httpRequest.getURL());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
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

    private HttpHeaders generateHeaders(HttpRequest httpRequest, byte[] body) {
        HttpHeaders headers = new HttpHeaders();

        String url = httpRequest.getURL();
        int extensionIndex = url.lastIndexOf(".") + 1;
        String extension = url.substring(extensionIndex);

        headers.setHeader(HttpHeaders.CONTENT_TYPE,
                List.of(HttpContentType.extensionToContentType(extension) + ";" + CHARSET_UTF_8));

        headers.setHeader(HttpHeaders.CONTENT_LENGTH, List.of(String.valueOf(body.length)));

        return headers;
    }
}
