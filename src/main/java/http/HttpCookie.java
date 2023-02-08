package http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpCookie {
    public static final int BEGIN_INDEX_OF_COOKIE_PARAMS = 8;
    private final Map<String, String> cookieMap = new HashMap<>();

    public HttpCookie(String cookieParams) {
        Arrays.stream(cookieParams.substring(BEGIN_INDEX_OF_COOKIE_PARAMS)
                        .split(";"))
                .forEach(this::setCookieParam);
    }

    public Optional<String> getCookieParam(String key) {
        return Optional.ofNullable(cookieMap.get(key));
    }

    private void setCookieParam(String cookieParam) {
        String[] token = cookieParam.trim()
                .split("=");
        String key = token[0];
        String value = token[1];
        this.cookieMap.put(key, value);
    }

    public Boolean isSessionId() {
        return cookieMap.containsKey("JSESSIONID");
    }

    public String getPath() {
        return cookieMap.getOrDefault("Path", "/");
    }
}
