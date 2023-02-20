package webserver.controller;

import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseContentType;
import webserver.utils.ResponseUtil;

import java.util.Arrays;

import static webserver.request.HttpRequestMethod.GET;
import static webserver.response.HttpResponseContentType.*;

public class ResourceController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String uri = request.getUri().getPath();
        String prefix = getFilePrefix(uri);
        byte[] body = FileIoUtils.loadFileFromClasspath(prefix + uri);
        ResponseUtil.response200(response, body, getStaticFileContentType(uri));
    }

    private String getFilePrefix(String uri) {
        if (isStaticFiles(uri)) {
            return "static";
        }
        if (isHtml(uri)) {
            return "templates";
        }
        return null;
    }

    private static HttpResponseContentType getStaticFileContentType(String uri) {
        return Arrays.stream(HttpResponseContentType.values())
                .filter(type -> uri.endsWith(type.getSuffix()))
                .findFirst()
                .orElseThrow();
    }

    private static boolean isStaticFiles(String uri) {
        return uri.endsWith(CSS.getSuffix()) || uri.endsWith(JS.getSuffix()) ||
                uri.endsWith(WOFF.getSuffix()) || uri.endsWith(TTF.getSuffix()) ||
                uri.endsWith(ICO.getSuffix());
    }

    private static boolean isHtml(String uri) {
        return uri.endsWith(".html");
    }

    @Override
    public boolean isMatch(HttpRequest request) {
        return request.getMethod() == GET &&
                isStaticFiles(request.getUri().getPath()) || isHtml(request.getUri().getPath());
    }
}
