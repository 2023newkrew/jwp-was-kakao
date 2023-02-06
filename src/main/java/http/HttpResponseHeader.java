package http;

import java.util.HashMap;
import java.util.Map;

class HttpResponseHeader {

    private final String version;
    private final HttpStatus status;
    private final Map<HttpHeaders, String> attributes;

    public HttpResponseHeader() {
        this("1.1", HttpStatus.OK);
    }

    public HttpResponseHeader(HttpStatus status) {
        this("1.1", status);
    }

    public HttpResponseHeader(String version, HttpStatus status) {
        this(version, status, new HashMap<>());
    }

    public HttpResponseHeader(String version, HttpStatus status, Map<HttpHeaders, String> attributes) {
        this.version = version;
        this.status = status;
        this.attributes = attributes;
    }

    protected byte[] getResponseHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/" + version + " " + status.getCode() + " " + status.getMessage() + " \r\n");
        attributes.forEach((key, value) -> {
            sb.append(key.getHeader() + ": " + value + " \r\n");
        });
        sb.append("\r\n");
        return sb.toString().getBytes();
    }
}
