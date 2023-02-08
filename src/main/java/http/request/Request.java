package http.request;

import http.HttpMethod;
import http.Uri;

public class Request {
    private final HttpMethod method;
    private final Uri uri;
    private final String version;
    private final RequestHeaders headers;
    private final RequestBody body;

    public Request(HttpMethod method, Uri uri, String version, RequestHeaders headers, RequestBody body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Uri getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }
}
