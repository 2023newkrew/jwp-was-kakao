package web.controller;

import http.Body;
import http.ContentType;
import http.HttpHeaders;
import http.request.HttpMethod;
import http.request.HttpRequest;
import http.response.HttpResponse;
import utils.IOUtils;

import java.util.Objects;

import static http.HttpHeaders.CONTENT_LENGTH;
import static http.HttpHeaders.CONTENT_TYPE;

public class GetResourceController implements Controller {

    private static final String HTML_SUFFIX = ".html";
    private static final String TEMPLATE_PATH = "templates";
    private static final String STATIC_PATH = "static";

    @Override
    public HttpResponse run(HttpRequest httpRequest) {
        String resourcePath = getSuffix(httpRequest) + httpRequest.getPath();
        return HttpResponse.ok(
                () -> new Body(IOUtils.readFileFromClasspath(resourcePath)),
                body -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.put(CONTENT_TYPE, ContentType.from(resourcePath).toString());
                    httpHeaders.put(CONTENT_LENGTH, String.valueOf(body.length()));

                    return httpHeaders;
                });
    }

    @Override
    public boolean isMatch(HttpRequest httpRequest) {
        return Objects.equals(HttpMethod.GET, httpRequest.getMethod());
    }

    private String getSuffix(HttpRequest httpRequest) {
        String path = httpRequest.getPath();

        if (path.contains(HTML_SUFFIX)) {
            return TEMPLATE_PATH;
        }

        return STATIC_PATH;
    }

}
