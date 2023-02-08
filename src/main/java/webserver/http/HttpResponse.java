package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpHeaders headers;
    private byte[] body = "".getBytes();
    private HttpStatus httpStatus;

    public HttpResponse() {
        headers = new HttpHeaders(new HashMap<>());
    }

    public String getHeader(String headerName) {
        return headers.getHeaders().get(headerName);
    }

    public byte[] getBody() {
        return body;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers.getHeaders();
    }

    public void setHeader(String header, String value) {
        headers.getHeaders().put(header, value);
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }

    public void setStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
