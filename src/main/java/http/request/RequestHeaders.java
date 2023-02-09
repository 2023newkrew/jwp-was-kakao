package http.request;

import http.HttpRequestHeader;
import http.cookie.HttpCookie;
import utils.RequestParsingUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RequestHeaders {
    private final Map<HttpRequestHeader, String> headers;

    private HttpCookie cookie;

    public RequestHeaders(Map<HttpRequestHeader, String> headers) {
        this.headers = Map.copyOf(headers);
    }

    public static RequestHeaders fromRawHeaders(List<String> rawHeaders) {
        return RequestParsingUtils.parseRequestHeader(rawHeaders);
    }

    public HttpCookie getCookie() {
        if (Objects.isNull(cookie)) {
            cookie = HttpCookie.fromQueryString(headers.getOrDefault(HttpRequestHeader.COOKIE, ""));
        }
        return cookie;
    }

    public Optional<String> get(HttpRequestHeader key) {
        return Optional.ofNullable(headers.getOrDefault(key, null));
    }
}
