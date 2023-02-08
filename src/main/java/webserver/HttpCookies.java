package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpCookies {
    private final Map<String, String> cookies = new HashMap<>();

    public Map<String, String> getCookies() {
        return new HashMap<>(cookies);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }
}
