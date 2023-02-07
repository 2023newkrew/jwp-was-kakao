package infra.http.request;

import infra.http.Body;
import infra.http.Headers;
import infra.http.HttpMessageBase;

public class HttpRequest extends HttpMessageBase {
    private RequestLine requestLine;

    public HttpRequest(RequestLine requestLine, Headers headers, Body body) {
        super(headers, body);
        this.requestLine = requestLine;
    }

    public String getUri() {
        return this.requestLine.getUri();
    }

    public String getVersion() {
        return this.requestLine.getVersion();
    }

    public String getMethod() {
        return this.requestLine.getMethod().value();
    }

    public boolean isGET() {
        return this.requestLine.isGET();
    }

    public boolean isPOST() {
        return this.requestLine.isPOST();
    }
}
