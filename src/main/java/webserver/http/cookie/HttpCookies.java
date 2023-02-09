package webserver.http.cookie;

import java.util.Map;

public class HttpCookies {
    private final Map<String, HttpCookie> cookies;

    public HttpCookies(final Map<String, HttpCookie> cookies) {
        this.cookies = cookies;
    }

    public Map<String, HttpCookie> getCookies() {
        return cookies;
    }

    public HttpCookie getCookie(String cookieName) {
        return cookies.get(cookieName);
    }
}
