package http;

public class HttpRequest {

    private RequestInfo requestInfo;
    private Headers headers;

    public HttpRequest(RequestInfo requestInfo, Headers headers) {
        this.requestInfo = requestInfo;
        this.headers = headers;
    }

    public String getPath() {
        return requestInfo.getPath();
    }

    public HttpMethod getMethod() {
        return requestInfo.getHttpMethod();
    }
}
