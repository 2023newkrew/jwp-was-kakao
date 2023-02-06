package model.response;

import model.request.RequestHeader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    private final Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    private ResponseHeader(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public static ResponseHeader of(Map map) {
        return new ResponseHeader((HashMap<String, String>) map);
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }
}
