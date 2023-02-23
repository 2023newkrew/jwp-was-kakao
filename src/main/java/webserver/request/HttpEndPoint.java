package webserver.request;

import java.net.URI;

public class HttpEndPoint {

    private final HttpRequestMethod method;
    private final URI uri;

    private HttpEndPoint(HttpRequestMethod method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    public static HttpEndPoint of(HttpRequestMethod method, URI uri) {
        return new HttpEndPoint(method, uri);
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }
}
