package http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestHeader {
    private final List<String> headers;
    private List<HttpCookie> cookies;

    public HttpRequestHeader(List<String> headers) {
        this.headers = headers;
        this.cookies = headers.stream()
                .filter(this::isCookieHeader)
                .map(HttpCookie::new)
                .collect(Collectors.toList());
    }

    public String getRequestMethod() {
        String[] token = headers.get(0)
                .split(" ");
        return token[0];
    }

    public String getRequestPath() {
        String[] token = headers.get(0)
                .split(" ");
        return token[1];
    }

    public Optional<Integer> getContentLength() {
        return headers.stream()
                .filter(line -> line.startsWith("Content-Length: "))
                .findFirst()
                .map(line -> line.substring(16))
                .map(String::trim)
                .map(Integer::parseInt);
    }

    public List<HttpCookie> getCookies() {
        return this.cookies;
    }

    private Boolean isCookieHeader(String header) {
        return header.startsWith("Cookie:");
    }

    public Optional<HttpCookie> getSessionId() {
        return cookies.stream()
                .filter(HttpCookie::isSessionId)
                .findFirst();
    }
}
