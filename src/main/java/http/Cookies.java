package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookies {
    public static final String COOKIE_DELIMITER = ";";
    private static final String KEY_VALUE_DELIMITER = "=";
    private static final int INDEX_OF_KEY = 0;
    private static final int INDEX_OF_VALUE = 1;

    private final Map<String, String> cookies = new HashMap<>();

    public Cookies() {
    }

    public Cookies(String cookie) {
        if (cookie == null || cookie.isBlank()) {
            return;
        }

        Arrays.stream(cookie.split(COOKIE_DELIMITER)).forEach(token -> {
            String[] keyValue = token.trim().split(KEY_VALUE_DELIMITER);
            if (keyValue.length == 2) {
                cookies.put(keyValue[INDEX_OF_KEY], keyValue[INDEX_OF_VALUE]);
            }
        });
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    public void setCookie(String key, String value) {
        cookies.put(key, value);
    }

    public String getCookieKeyValues() {
        return cookies.keySet().stream().map((key) -> (key + KEY_VALUE_DELIMITER + cookies.get(key))).collect(Collectors.joining(COOKIE_DELIMITER));
    }
}
