package webserver.http;

import java.util.Map;

public class HttpCookies {
    private final Map<String, String> cookies;

    public HttpCookies(final Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getCookie(String cookieName) {
        return cookies.get(cookieName);
    }
}
