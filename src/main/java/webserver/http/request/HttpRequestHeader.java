package webserver.http.request;

import webserver.http.cookie.HttpCookie;

import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String COOKIE = "Cookie";
    private final Map<String, String> headers;
    private final HttpCookie cookie;

    public HttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
        this.cookie = parseCookieFromHeader();
    }

    public Optional<Integer> getContentLength() {
        return Optional.ofNullable(headers.get(CONTENT_LENGTH))
                .map(Integer::parseInt);
    }

    public Optional<String> getCookie(String key) {
        if (cookie == null) {
            return Optional.empty();
        }
        return cookie.getCookie(key);
    }

    private HttpCookie parseCookieFromHeader() {
        String cookieHeader = headers.get(COOKIE);
        if (cookieHeader == null) {
            return null;
        }
        return HttpCookie.from(cookieHeader);
    }
}
