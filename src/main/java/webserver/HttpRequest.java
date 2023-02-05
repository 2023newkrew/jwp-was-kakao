package webserver;

import java.net.URI;
import java.util.Optional;

public class HttpRequest {

    private final HttpRequestHeader httpRequestHeader;
    private final HttpReqeustBody httpReqeustBody;

    public HttpRequest(HttpRequestHeader httpRequestHeader, HttpReqeustBody httpReqeustBody) {
        this.httpRequestHeader = httpRequestHeader;
        this.httpReqeustBody = httpReqeustBody;
    }

    public Optional<String> getAttribute(String key) {
        return httpRequestHeader.getAttribute(key);
    }

    public String getMethod() {
        return httpRequestHeader.getMethod();
    }

    public URI getUri() {
        return httpRequestHeader.getUri();
    }

    public String getHttpVersion() {
        return httpRequestHeader.getHttpVersion();
    }

    public String getBody() {
        return httpReqeustBody.getContent();
    }
}
