package webserver.http.request;

import webserver.enums.RequestMethod;

public class HttpRequest {
    private final RequestMethod requestMethod;
    private final String requestURL;
    private final HttpRequestHeader headers;
    private final String body;

    private HttpRequest(RequestMethod requestMethod, String requestURL, HttpRequestHeader headers, String body) {
        this.requestMethod = requestMethod;
        this.requestURL = requestURL;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest of(RequestMethod requestMethod, String requestPath, HttpRequestHeader headers, String body) {
        return new HttpRequest(requestMethod, requestPath, headers, body);
    }

    public String getRequestURL() {
        return requestURL;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public HttpRequestHeader getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
