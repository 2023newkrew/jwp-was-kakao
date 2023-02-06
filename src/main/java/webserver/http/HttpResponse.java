package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body = "".getBytes();
    private HttpStatus httpStatus;

    public HttpResponse() {
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }

    public void setStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
