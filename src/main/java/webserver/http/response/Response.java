package webserver.http.response;

import webserver.http.content.Content;

public class Response {
    private final ResponseHeader header;

    private final Content body;

    public Response(ResponseHeader header) {
        this(header, null);
    }

    public Response(ResponseHeader header, Content body) {
        this.header = header;
        this.body = body;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public Content getBody() {
        return body;
    }
}
