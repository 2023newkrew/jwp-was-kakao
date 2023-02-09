package webserver.request;

import java.net.URI;
import java.util.Optional;

public class HttpRequest {

    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    private HttpRequest(HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest of(HttpRequestHeader header, HttpRequestBody body) {
        return new HttpRequest(header, body);
    }

    public Optional<String> getAttribute(String key) {
        return httpRequestHeader.getAttribute(key);
    }

    public HttpRequestMethod getMethod() {
        return httpRequestHeader.getMethod();
    }

    public URI getUri() {
        return httpRequestHeader.getUri();
    }

    public String getHttpVersion() {
        return httpRequestHeader.getHttpVersion();
    }

    public String getBody() {
        return httpRequestBody.getContent();
    }
}
