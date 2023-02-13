package controller;

import common.*;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class FileController implements Controller {
    private static final String HTML_FILE_PATH = "./templates";
    private static final String STATIC_FILE_PATH = "./static";

    public void process(HttpRequest request, HttpResponse response) {
        try {
            if (request.getMethod().equals(HttpMethod.GET)) {
                response.setBody(getFileContent(request.getUri()));
                response.setHttpStatus(HttpStatus.OK);

                if (request.getUri().endsWith(".css")) {
                    response.setHeader(HttpHeader.CONTENT_TYPE, "text/css;charset=utf-8");
                }
                else if (request.getUri().endsWith(".js")) {
                    response.setHeader(HttpHeader.CONTENT_TYPE, "text/javascript;charset=utf-8");
                }
            }
        } catch (Exception e) {
            response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setBody(e.getMessage().getBytes());
        }
    }

    public byte[] getFileContent(final String uri) throws IOException, URISyntaxException {
        if (uri.endsWith(".html")) {
            return FileIoUtils.loadFileFromClasspath(HTML_FILE_PATH + uri.replaceFirst("^\\.+", "").replaceAll("\\.\\.\\/", ""));
        }
        else if (uri.endsWith(".css")) {
            return FileIoUtils.loadFileFromClasspath(STATIC_FILE_PATH + uri.replaceFirst("^\\.+", "").replaceAll("\\.\\.\\/", ""));
        }
        else if (uri.endsWith(".js")) {
            return FileIoUtils.loadFileFromClasspath(STATIC_FILE_PATH + uri.replaceFirst("^\\.+", "").replaceAll("\\.\\.\\/", ""));
        }
        else {
            return "".getBytes();
        }
    }
}
