package model.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private final Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    private RequestHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public static RequestHeader of(Map map) {
        return new RequestHeader((HashMap<String, String>) map);
    }
}
