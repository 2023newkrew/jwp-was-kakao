package controller;

import common.HttpMethod;
import common.HttpRequest;
import common.HttpResponse;
import common.HttpStatus;
import utils.FileIoUtils;
import webserver.*;

import java.io.IOException;
import java.net.URISyntaxException;

public class FileController implements Controller {
    private static final String HTML_FILE_PATH = "./templates";
    private static final String CSS_FILE_PATH = "./static";

    public void process(HttpRequest request, HttpResponse response) {
        try {
            if (request.getMethod().equals(HttpMethod.GET)) {
                response.setBody(getFileContent(request.getUri()));
                response.setHttpStatus(HttpStatus.OK);
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public byte[] getFileContent(final String uri) throws IOException, URISyntaxException {
        if (uri.endsWith(".html")) {
            return FileIoUtils.loadFileFromClasspath(HTML_FILE_PATH + uri.replaceFirst("^\\.+", ""));
        }
        else if (uri.endsWith(".css")) {
            return FileIoUtils.loadFileFromClasspath(CSS_FILE_PATH + uri.replaceFirst("^\\.+", ""));
        }
        else {
            return "".getBytes();
        }
    }
}
