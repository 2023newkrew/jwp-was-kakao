package http.response;

import http.Cookies;
import http.HttpHeader;
import http.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import utils.HandlebarsUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    public static final String TEMPLATE_PATH = "./templates/";
    public static final String STATIC_PATH = "./static/";

    private final DataOutputStream dos;
    private final Cookies cookies;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
        this.cookies = new Cookies();
    }

    public void forward(String path) {
        byte[] body = FileIoUtils.loadFileFromClasspath(getResourcePath(path));
        logger.info("Forward to file : {}", path);
        response(HttpStatus.OK, ContentType.from(path), null, body);
    }

    public void forward(String path, Object model) {
        String view = HandlebarsUtils.getView(path, model);
        response(HttpStatus.OK, null, null, view.getBytes());
    }

    private String getResourcePath(String path) {
        if (path.endsWith(".html")) {
            return TEMPLATE_PATH + path;
        }
        return STATIC_PATH + path;
    }

    private void response(HttpStatus status, ContentType type, Map<String, String> headers, byte[] body) {
        responseHeader(status, type, body.length);
        responseOptionalHeader(headers);
        responseCookies();
        responseBody(body);
    }

    private void responseHeader(HttpStatus status, ContentType contentType, int length) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %s\r\n", status.toString()));
            if (contentType != null) {
                dos.writeBytes("Content-Type: " + contentType.getValue() + "\r\n");
            }
            dos.writeBytes("Content-Length: " + length + "\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseOptionalHeader(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }

        headers.forEach((key, value) -> {
            try {
                dos.writeBytes(String.format("%s: %s\r\n", key, value));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void responseCookies() {
        String cookies = this.cookies.getCookieKeyValues();
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        try {
            dos.writeBytes("Set-Cookie: " + cookies + "\r\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendRedirect(String path) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", path);
        response(HttpStatus.FOUND, null, headers, "".getBytes());
    }

    public void addCookie(String key, String value) {
        cookies.setCookie(key, value);
    }
}
