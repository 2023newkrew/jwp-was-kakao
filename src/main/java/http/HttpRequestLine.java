package http;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

public class HttpRequestLine {
    private final String method;
    private final URI uri;
    private final String httpVersion;
    private final Map<String, String> params;

    private HttpRequestLine(String method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.params = QueryParser.parse(uri.getQuery());
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
}
