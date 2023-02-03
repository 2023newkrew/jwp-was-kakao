package webserver;

import java.util.Map;

public class HttpResponse {
    private Map<String, String> headers;
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

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }

    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
