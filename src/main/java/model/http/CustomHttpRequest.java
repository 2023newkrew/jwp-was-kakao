package model.http;

public class CustomHttpRequest {

    private final CustomHttpRequestLine line;
    private final CustomHttpHeader headers;
    private final CustomHttpRequestBody body;

    public CustomHttpRequest(CustomHttpRequestLine line, CustomHttpHeader headers, CustomHttpRequestBody body) {
        this.line = line;
        this.headers = headers;
        this.body = body;
    }

    public CustomHttpRequestLine getHttpRequestLine() {
        return line;
    }

    public CustomHttpHeader getHeaders() {
        return headers;
    }

    public CustomHttpRequestBody getBody() {
        return body;
    }
}
