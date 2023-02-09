package webserver.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Cookie {

    private final Map<String, String> cookies;

    public Cookie(String cookieString) {
        cookies = new HashMap<>();
        if (cookieString != null) {
            parseCookies(cookieString);
        }
    }

    private void parseCookies(String cookieString) {
        String[] cookies = cookieString.split(";");
        Arrays.stream(cookies).forEach(this::parseCookie);
    }

    private void parseCookie(String cookie) {
        String[] stringCookie = cookie.split("=");
        cookies.put(stringCookie[0], stringCookie[1]);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
