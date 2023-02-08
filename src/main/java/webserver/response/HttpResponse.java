package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import utils.HttpRequestUtils;
import webserver.HttpCookie;
import webserver.request.HttpRequest;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private HttpStatusCode httpStatusCode;
    private Map<String, String> header;
    private byte[] body;

    private HttpResponse(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
        this.header = new LinkedHashMap<>();
    }

    public static HttpResponse ok(HttpRequest httpRequest, byte[] body, String contentType) {
        HttpResponse httpResponse = new HttpResponse(HttpStatusCode.OK);
        httpResponse.addHeader("Content-Type", contentType);
        httpResponse.addHeader("Content-Length", String.valueOf(body.length));
        httpResponse.setBody(body);
        httpResponse.setCookie(httpRequest);

        return httpResponse;
    }

    private void setCookie(HttpRequest httpRequest) {
        HttpCookie httpCookie = new HttpCookie(httpRequest.getHeader("Cookie"));
        if (httpCookie.getCookie(HttpCookie.SESSION_ID_NAME) == null) {
            addHeader("Set-Cookie", HttpRequestUtils.generateRandomCookieString());
        }
    }

    public static HttpResponse redirect(HttpRequest httpRequest, String location) {
        HttpResponse httpResponse = new HttpResponse(HttpStatusCode.FOUND);
        httpResponse.addHeader("Location", location);
        httpResponse.setCookie(httpRequest);

        return httpResponse;
    }

    public static HttpResponse pageNotFound() {
        return new HttpResponse(HttpStatusCode.NOT_FOUND);
    }

    private void addHeader(String key, String value) {
        header.put(key, value);
    }

    private void setBody(byte[] body) {
        this.body = body;
    }

    public void response(DataOutputStream dos) throws IOException {
        writeHeader(dos);
        if (body.length > 0) {
            writeBody(dos);
        }

        dos.flush();
    }

    private void writeHeader(DataOutputStream dos) throws IOException {
        dos.writeBytes(HTTP_VERSION + " " + httpStatusCode.getStatusCode() + " " + httpStatusCode.getMessage() + " \r\n");
        header.forEach((key, value) -> {
            try {
                dos.writeBytes(key + ": " + value + " \r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dos.writeBytes("\r\n");
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }
}
