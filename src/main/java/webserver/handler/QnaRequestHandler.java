package webserver.handler;

import http.*;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class QnaRequestHandler implements UrlMappingHandler {

    private static final String URL_MAPPING_REGEX = "/qna/.+";
    private static final String TEMPLATES_FILEPATH = "./templates";

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath(TEMPLATES_FILEPATH + httpRequest.getURL());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }

        httpResponse.setStatus(HttpStatus.OK);
        httpResponse.addHeaders(generateHeaders(httpRequest, bytes));
        httpResponse.setBody(bytes);
    }

    @Override
    public boolean support(HttpRequest httpRequest) {
        return httpRequest.getMethod() == HttpMethod.GET;
    }

    @Override
    public String getUrlMappingRegex() {
        return URL_MAPPING_REGEX;
    }

    private HttpHeaders generateHeaders(HttpRequest httpRequest, byte[] body) {
        HttpHeaders headers = new HttpHeaders();

        String url = httpRequest.getURL();
        int extensionIndex = url.lastIndexOf(".") + 1;
        String extension = url.substring(extensionIndex);

        headers.setHeader(HttpHeaders.CONTENT_TYPE,
                List.of(HttpContentType.fromExtensionAndCharset(extension, StandardCharsets.UTF_8)));

        headers.setHeader(HttpHeaders.CONTENT_LENGTH, List.of(String.valueOf(body.length)));

        return headers;
    }
}
