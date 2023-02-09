package webserver.request;

import java.net.URI;

public class HttpEndPoint {

    private final String method;
    private final URI uri;

    private HttpEndPoint(String method, URI uri) {
        this.method = method;
        this.uri = uri;
    }

    public static HttpEndPoint of(String method, URI uri) {
        return new HttpEndPoint(method, uri);
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }
}
