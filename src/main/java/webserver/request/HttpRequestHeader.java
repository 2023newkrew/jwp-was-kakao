package webserver.request;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final HttpEndPoint httpEndPoint;
    private final String httpVersion;

    private HttpRequestHeader(HttpRequestMethod method, URI uri, String httpVersion) {
        this.httpEndPoint = HttpEndPoint.of(method, uri);
        this.httpVersion = httpVersion;
    }

    public static HttpRequestHeader of(String method, String uri, String httpVersion) {
        return new HttpRequestHeader(HttpRequestMethod.from(method), URI.create(uri), httpVersion);
    }

    public Optional<String> getAttribute(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void addAttribute(String key, String value) {
        headers.put(key, value);
    }

    public Optional<String> getCookie(String name) {
        return Optional.ofNullable(cookies.get(name));
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
    }

    public HttpRequestMethod getMethod() {
        return httpEndPoint.getMethod();
    }

    public URI getUri() {
        return httpEndPoint.getUri();
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
