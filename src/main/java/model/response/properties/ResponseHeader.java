package model.response.properties;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ResponseHeader {
    private final LinkedHashMap<String, String> headers;

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public ResponseHeader() {
        this.headers = new LinkedHashMap<>();
    }

    public void setAttribute(String key, String value) {
        headers.put(key, value);
    }

    public Set<Map.Entry<String, String>> getEntrySet() {
        return headers.entrySet();
    }
}
