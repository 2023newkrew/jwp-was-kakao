package http.request;

import http.Protocol;

public class RequestInfo {

    private final HttpMethod httpMethod;
    private final HttpUrl httpUrl;
    private final Protocol protocol;

    public RequestInfo(HttpMethod httpMethod, HttpUrl httpUrl, Protocol protocol) {
        this.httpMethod = httpMethod;
        this.httpUrl = httpUrl;
        this.protocol = protocol;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return httpUrl.getPath();
    }
}
