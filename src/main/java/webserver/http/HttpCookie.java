package webserver.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpCookie {
    private final Map<String, String> cookies = new ConcurrentHashMap<>();

    public Map<String, String> getCookies() {
        return cookies;
    }
}
