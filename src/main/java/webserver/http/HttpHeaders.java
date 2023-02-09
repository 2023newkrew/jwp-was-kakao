package webserver.http;

import java.util.Map;

public class HttpHeaders {
    private final Map<String, HttpHeader> headers;

    public HttpHeaders(final Map<String, HttpHeader> headers) {
        this.headers = headers;
    }

    public Map<String, HttpHeader> getHeaders() {
        return headers;
    }

    public HttpHeader getHeader(String headerName) {
        return headers.get(headerName);
    }
}
