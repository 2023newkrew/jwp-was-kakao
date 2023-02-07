package http;

import com.google.common.collect.ImmutableCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpHeaders {

    private static final String HEADER_DELIMITER = ": ";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String LOCATION = "Location";

    private final Map<String, String> headers;

    public HttpHeaders() {
        this.headers = new HashMap<>();
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public boolean containsKey(String key) {
        return headers.containsKey(key);
    }

    public String get(String key) {
        return headers.get(key);
    }

    public void put(String str) {
        String[] split = str.split(HEADER_DELIMITER);
        put(split[0], split[1]);
    }

    public String getOrDefault(String key, String defaultValue) {
        return headers.getOrDefault(key, defaultValue);
    }

    public String toString() {
        return headers.keySet()
                .stream()
                .map(key -> String.format("%s: %s ", key, headers.get(key)))
                .collect(Collectors.joining("\r\n"));
    }
}
