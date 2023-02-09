package auth;

import java.util.UUID;

public class HttpCookie {
    private final String cookie;
    private final String path;

    public HttpCookie(UUID cookie) {
        this.cookie = cookie.toString();
        this.path = "/";
    }

    public HttpCookie(UUID cookie, String path) {
        this.cookie = cookie.toString();
        this.path = path;
    }

    public String getCookie() {
        return cookie;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "JSESSIONID=" + this.cookie.toString() + "; Path=" + this.path;
    }
}
