package http;

import java.util.HashMap;
import java.util.Map;

class HttpResponseHeader {
    private final Map<HttpHeaders, String> headers;

    public HttpResponseHeader(Map<HttpHeaders, String> headers) {
        this.headers = headers;
    }

    protected byte[] getBytes() {
        StringBuilder sb = new StringBuilder();
        headers.forEach((key, value) -> {
            sb.append(key.getHeader() + ": " + value + " \r\n");
        });
        sb.append("\r\n");
        return sb.toString().getBytes();
    }
}
