package webserver.request;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequestHeader {

    private final Map<String, String> headers = new HashMap<>();
    private final String method;
    private final URI uri;
    private final String httpVersion;

    private HttpRequestHeader(String method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestHeader of(String method, String uri, String httpVersion) {
        return new HttpRequestHeader(method, URI.create(uri), httpVersion);
    }

    public Optional<String> getAttribute(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void addAttribute(String key, String value) {
        headers.put(key, value);
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
