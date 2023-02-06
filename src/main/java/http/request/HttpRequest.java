package http.request;

import http.Body;
import http.Headers;

public class HttpRequest {

    private RequestInfo requestInfo;
    private Headers headers;
    private Body body;

    public HttpRequest(RequestInfo requestInfo, Headers headers) {
        this.requestInfo = requestInfo;
        this.headers = headers;
    }

    public HttpRequest(RequestInfo requestInfo, Headers headers, Body body) {
        this(requestInfo, headers);
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
