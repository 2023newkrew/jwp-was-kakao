package http;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpCookies {

    public static final String SESSION_ID = "JSESSIONID";
    public static final String PATH = "Path";
    private static final String COOKIE_DELIMITER = "; ";
    private static final String KEY_VALUE_DELIMITER = "=";

    private final Map<String, String> cookies;

    public HttpCookies() {
        this.cookies = new LinkedHashMap<>();
    }

    private HttpCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public static HttpCookies from(String cookies) {
         return new HttpCookies(Arrays.stream(cookies.split(COOKIE_DELIMITER))
                 .map(cookie -> cookie.split(KEY_VALUE_DELIMITER))
                 .collect(Collectors.toMap(cookie -> cookie[0], cookie -> cookie[1])));
    }

    public void put(String key, String value) {
        cookies.put(key, value);
    }

    public String get(String key) {
        return cookies.get(key);
    }

    @Override
    public String toString() {
        return cookies.keySet()
                .stream()
                .map(key -> String.format("%s%s%s", key, KEY_VALUE_DELIMITER, cookies.get(key)))
                .collect(Collectors.joining(COOKIE_DELIMITER));
    }
}
