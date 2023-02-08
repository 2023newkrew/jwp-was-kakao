package webserver.request;

import org.apache.tomcat.Jar;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {

    private static final String JSESSIONID = "JSESSIONID";
    private static final String COOKIE_VALUE_SEPARATOR = ";";
    private static final String KEY_VALUE_SEPARATOR = "=";

    private final Map<String, String> cookieValues;

    public HttpCookie() {
        this.cookieValues = new HashMap<>();
    }

    public HttpCookie(Map<String, String> cookieValues) {
        this.cookieValues = cookieValues;
    }

    public static HttpCookie of(String cookieValue) {
        final Map<String, String> cookieValues = new HashMap<>();
        for (String cookie : cookieValue.split(COOKIE_VALUE_SEPARATOR)) {
            final String[] cookies = cookie.split(KEY_VALUE_SEPARATOR, 2);
            cookieValues.put(cookies[0].trim(), cookies[1].trim());
        }
        return new HttpCookie(cookieValues);
    }

    public String getSessionId() {
        return cookieValues.get(JSESSIONID);
    }
}
