package controller;

import enums.ContentType;
import http.HttpRequest;
import http.HttpResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticFileController {
    private static StaticFileController instance;

    public static StaticFileController getInstance() {
        if (instance == null) {
            instance = new StaticFileController();
        }
        return instance;
    }

    public HttpResponse staticFileGet(HttpRequest request) throws IOException, URISyntaxException {
        String requestPath = request.getRequestPath();
        ContentType contentType = ContentType.fromFilename(requestPath);

        String resourcePath = FileIoUtils.getResourcePath(requestPath, contentType);
        byte[] body = FileIoUtils.loadFileFromClasspath(resourcePath);

        return HttpResponse.of(HttpStatus.OK, contentType, body);
    }

    public HttpResponse indexGet(){
        return HttpResponse.create302FoundResponse("/index.html");
    }
}
