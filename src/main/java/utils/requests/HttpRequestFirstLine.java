package utils.requests;

import java.net.URI;

public class HttpRequestFirstLine {
    private final RequestMethod requestMethod;
    private final URI uri;
    private final String httpVersion;

    public HttpRequestFirstLine(RequestMethod requestMethod, URI uri, String httpVersion) {
        this.requestMethod = requestMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public URI getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
