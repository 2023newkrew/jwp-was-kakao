package webserver.http;

import java.util.Map;

public class Cookies {
    private final Map<String, String> cookies;

    public Cookies(final Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getCookie(String cookieName) {
        return cookies.get(cookieName);
    }
}
