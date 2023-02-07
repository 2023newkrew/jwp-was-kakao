package http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpCookie {

    public static final String SESSION_ID = "JSESSIONID";
    public static final String PATH = "Path";

    private final Map<String, String> cookies;

    public HttpCookie() {
        this.cookies = new LinkedHashMap<>();
    }

    public void put(String key, String value) {
        cookies.put(key, value);
    }

    @Override
    public String toString() {
        return cookies.keySet()
                .stream()
                .map(key -> String.format("%s=%s", key, cookies.get(key)))
                .collect(Collectors.joining("; "));
    }
}
