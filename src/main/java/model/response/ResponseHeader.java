package model.response;

import model.request.RequestHeader;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHeader {
    private final LinkedHashMap<String, String> headers;

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    private ResponseHeader(Map<String, String> headers) {
        this.headers = (LinkedHashMap<String, String>) headers;
    }

    public static ResponseHeader of(Map map) {
        return new ResponseHeader(map);
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }
}
