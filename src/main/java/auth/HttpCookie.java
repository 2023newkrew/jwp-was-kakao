package auth;

import java.util.UUID;

public class HttpCookie {
    private final UUID cookie;
    private final String path;

    public HttpCookie(UUID cookie) {
        this.cookie = cookie;
        this.path = "/";
    }

    public HttpCookie(UUID cookie, String path) {
        this.cookie = cookie;
        this.path = path;
    }

    public UUID getCookie() {
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
