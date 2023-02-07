package http;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpRequestLine {
    private final String method;
    private final URI uri;
    private final String httpVersion;
    private final Map<String, String> params;

    protected HttpRequestLine(String method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.params = UriQueryParser.parse(uri.getQuery());
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public Optional<String> getParam(String key) {
        return Optional.ofNullable(params.get(key));
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public static HttpRequestLine from(String message) {
        String[] lines = message.split(" ");
        return new HttpRequestLine(
                lines[0], URI.create(lines[1]), lines[2]
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestLine that = (HttpRequestLine) o;
        return Objects.equals(method, that.method) && Objects.equals(uri, that.uri) && Objects.equals(httpVersion, that.httpVersion) && Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uri, httpVersion, params);
    }

    @Override
    public String toString() {
        return "HttpRequestLine{" +
                "method='" + method + '\'' +
                ", uri=" + uri +
                ", httpVersion='" + httpVersion + '\'' +
                ", params=" + params +
                '}';
    }
}
