package webserver;

import java.util.ArrayList;
import java.util.List;

public class HttpCookies {

    private final List<HttpCookie> cookies;

    public HttpCookies() {
        cookies = new ArrayList<>();
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }
}
