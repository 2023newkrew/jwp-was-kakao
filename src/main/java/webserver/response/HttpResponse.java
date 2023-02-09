package webserver.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import utils.HttpRequestUtils;
import webserver.request.HttpRequest;
import webserver.session.HttpSession;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final HttpStatusCode httpStatusCode;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private byte[] body;

    private HttpResponse(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
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
        HttpSession httpSession = httpRequest.getHttpSession();
        if (!httpRequest.isRequestedSessionId()) {
            addHeader("Set-Cookie", HttpRequestUtils.convertSessionIdToString(httpSession.getId()));
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
        headers.put(key, value);
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
        dos.writeBytes(
                HTTP_VERSION + " " + httpStatusCode.getStatusCode() + " " + httpStatusCode.getMessage() + " \r\n");
        headers.forEach((key, value) -> {
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
