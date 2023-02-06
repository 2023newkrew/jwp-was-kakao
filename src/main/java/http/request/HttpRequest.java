package http.request;

import http.Body;
import http.HttpHeaders;

public class HttpRequest {

    private final RequestInfo requestInfo;
    private final HttpHeaders httpHeaders;
    private final Body body;

    public HttpRequest(RequestInfo requestInfo, HttpHeaders httpHeaders) {
        this(requestInfo, httpHeaders, null);
    }

    public HttpRequest(RequestInfo requestInfo, HttpHeaders httpHeaders, Body body) {
        this.requestInfo = requestInfo;
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    public String getPath() {
        return requestInfo.getPath();
    }

    public HttpMethod getMethod() {
        return requestInfo.getHttpMethod();
    }

    public String getBody() {
        return new String(body.asByte());
    }
}
