package http;

import common.Protocol;

public class RequestInfo {

    private HttpMethod httpMethod;
    private HttpUrl httpUrl;
    private Protocol protocol;

    public RequestInfo(HttpMethod httpMethod, HttpUrl httpUrl, Protocol protocol) {
        this.httpMethod = httpMethod;
        this.httpUrl = httpUrl;
        this.protocol = protocol;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
