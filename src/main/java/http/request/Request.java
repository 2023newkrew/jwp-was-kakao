package http.request;

public class Request {
    private final RequestStartLine startLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public Request(RequestStartLine startLine, RequestHeaders headers, RequestBody body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public RequestStartLine getStartLine() {
        return startLine;
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }
}
