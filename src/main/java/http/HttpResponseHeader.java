package http;

import java.util.HashMap;
import java.util.Map;

class HttpResponseHeader {

    private final String version;
    private final HttpStatus status;
    private final Map<HttpHeaders, String> headers;

    public HttpResponseHeader() {
        this("1.1", HttpStatus.OK);
    }

    public HttpResponseHeader(HttpStatus status) {
        this("1.1", status);
    }

    public HttpResponseHeader(String version, HttpStatus status) {
        this(version, status, new HashMap<>());
    }

    public HttpResponseHeader(String version, HttpStatus status, Map<HttpHeaders, String> headers) {
        this.version = version;
        this.status = status;
        this.headers = headers;
    }

    protected byte[] getResponseHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/" + version + " " + status.getCode() + " " + status.getMessage() + " \r\n");
        headers.forEach((key, value) -> {
            sb.append(key.getHeader() + ": " + value + " \r\n");
        });
        sb.append("\r\n");
        return sb.toString().getBytes();
    }
}
