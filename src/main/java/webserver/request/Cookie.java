package webserver.request;

import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private final Map<String, String> cookies;

    public Cookie(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static Cookie parse(String cookieString) {
        Map<String, String> cookies = new HashMap<>();
        if (cookieString.isBlank()) {
            return new Cookie(cookies);
        }
        String[] cookieRows = cookieString.split(";");
        for (String cookieRow : cookieRows) {
            String[] splitCookie = cookieRow.split("=");
            cookies.put(splitCookie[0].strip(), splitCookie[1].strip());
        }
        return new Cookie(cookies);
    }

    public String get(String key) {
        return cookies.getOrDefault(key, "");
    }

    public int size() {
        return cookies.size();
    }
}
