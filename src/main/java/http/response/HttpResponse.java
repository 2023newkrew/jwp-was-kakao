package http.response;

import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String TEMPLATE_PATH = "./templates/";
    public static final String STATIC_PATH = "./static/";

    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(String path) {
        byte[] body = FileIoUtils.loadFileFromClasspath(getResourcePath(path));
        logger.info("Forward to file : {}", path);
        response(HttpStatus.OK, ContentType.from(path), body);
    }

    private String getResourcePath(String path) {
        if (path.endsWith(".html")) {
            return TEMPLATE_PATH + path;
        }
        return STATIC_PATH + path;
    }

    private void response(HttpStatus status, ContentType type, byte[] body) {
        responseHeader(status, type, body.length);
        responseBody(body);
    }

    private void responseHeader(HttpStatus status, ContentType contentType, int length) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %s\r\n", status.toString()));
            dos.writeBytes("Content-Type: " + contentType.getValue() + "\r\n");
            dos.writeBytes("Content-Length: " + length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendRedirect(String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes(String.format("Location: %s \r\n", path));
            dos.writeBytes("Content-Length: 0");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
