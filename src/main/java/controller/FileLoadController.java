package controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import utils.FileIoUtils;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public class FileLoadController implements Controller {
    @Override
    public HttpResponse response(HttpRequest httpRequest) {
        try {
            String path = httpRequest.getPath();
            byte[] body = FileIoUtils.getBodyFromPath(path);
            String contentType = Files.probeContentType(new File(path).toPath());
            return HttpResponse.ok(httpRequest, body, contentType);
        } catch (IOException | URISyntaxException e) {
            return HttpResponse.pageNotFound();
        }
    }
}
