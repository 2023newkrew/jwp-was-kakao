package http.request;

import http.HttpMethod;
import http.Uri;

import java.util.List;
import java.util.Map;

import static utils.ParsingUtils.*;

public class Request {
    private HttpMethod method;
    private Uri uri;
    private String version;
    private RequestHeaders headers;
    private RequestBody body;

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
