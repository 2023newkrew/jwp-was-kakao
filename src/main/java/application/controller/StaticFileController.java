package application.controller;

import webserver.enums.ContentType;
import org.springframework.http.HttpStatus;
import webserver.utils.FileIoUtils;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class StaticFileController {
    private static StaticFileController instance;

    private StaticFileController() {
    }

    public static StaticFileController getInstance() {
        if (instance == null) {
            instance = new StaticFileController();
        }
        return instance;
    }

    public HttpResponse staticFileGet(HttpRequest request) throws IOException, URISyntaxException {
        String requestPath = request.getRequestURL();
        ContentType contentType = ContentType.fromFilename(requestPath);

        String resourcePath = FileIoUtils.getResourcePath(requestPath, contentType);
        byte[] body = FileIoUtils.loadFileFromClasspath(resourcePath);

        return HttpResponse
                .status(HttpStatus.OK)
                .contentType(contentType)
                .body(new String(body));
    }
}
