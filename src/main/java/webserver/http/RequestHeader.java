package webserver.http;

import java.util.Map;

public class RequestHeader {
    private final Map<String, String> headers;
    public RequestHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    public String get(String key) {
        return headers.getOrDefault(key, "");
    }
}
