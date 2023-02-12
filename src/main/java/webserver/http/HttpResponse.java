package webserver.http;

import webserver.http.header.HttpHeader;
import webserver.http.header.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final HttpHeaders headers;
    private HttpStatus httpStatus;
    private byte[] body = "".getBytes();

    public HttpResponse() {
        headers = new HttpHeaders(new HashMap<>());
    }

    public Map<String, HttpHeader> getHeaders() {
        return headers.getHeaders();
    }

    public byte[] getBody() {
        return body;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public void setHeader(String header, String value) {
        headers.getHeaders().put(header, new HttpHeader(header, value));
    }

    public void setBody(final byte[] body) {
        this.body = body;
    }

    public void setStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
