package infra.http.request;

import infra.http.Headers;
import infra.http.HttpMessageBase;

public class HttpRequest extends HttpMessageBase {
    private RequestLine requestLine;
    private String requestBody;

    public HttpRequest(RequestLine requestLine, Headers headers, String requestBody) {
        super(headers);
        this.requestLine = requestLine;
        this.requestBody = requestBody;
    }

    public String getUri() {
        return this.requestLine.getUri();
    }

    public String getVersion() {
        return this.requestLine.getVersion();
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    public boolean isGET() {
        return this.requestLine.isGET();
    }

    public boolean isPOST() {
        return this.requestLine.isPOST();
    }
}
