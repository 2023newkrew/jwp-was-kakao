package webserver.request;

import java.util.Map;

public class Request {
    private final StartLine startLine;

    private final Map<String, String> headers;
    private final Map<String, String> body;


    public Request(StartLine startLine, Map<String, String> headers, Map<String, String> body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public String getAcceptContentType() {
        return headers.get("Accept").split(",")[0];
    }
}
