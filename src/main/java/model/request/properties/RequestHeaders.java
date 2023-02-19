package model.request.properties;

import java.util.Collections;
import java.util.Map;

public class RequestHeaders {
    private final Map<String, String> headers;

    private RequestHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static RequestHeaders of(Map<String, String> map) {
        return new RequestHeaders(map);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
