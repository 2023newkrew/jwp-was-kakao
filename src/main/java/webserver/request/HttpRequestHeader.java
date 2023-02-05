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

    public HttpRequestHeader(String method, String uri, String httpVersion) {
        this.method = method;
        this.uri = URI.create(uri);
        this.httpVersion = httpVersion;
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
