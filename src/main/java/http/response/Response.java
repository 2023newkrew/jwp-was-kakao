package http.response;

public class Response {
    private final String statusLine;
    private final String headers;
    private final byte[] body;

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public Response(String statusLine, String headers, byte[] body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
