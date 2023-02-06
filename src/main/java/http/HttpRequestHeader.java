package http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class HttpRequestHeader {
    private final Map<HttpHeaders, String> headers = new HashMap<>();
    private final String method;
    private final URI uri;
    private final String httpVersion;

    public HttpRequestHeader(String method, String uri, String httpVersion) {
        this.method = method;
        this.uri = URI.create(uri);
        this.httpVersion = httpVersion;
    }

    public Optional<String> getAttribute(HttpHeaders key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void addAttribute(HttpHeaders header, String value) {
        headers.put(header, value);
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
