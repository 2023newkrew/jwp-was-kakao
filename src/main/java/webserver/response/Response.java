package webserver.response;

import webserver.content.Content;

public class Response {
    private final ResponseHeader header;

    private final Content body;

    public Response(ResponseHeader header) {
        this(header, null);
    }

    public Response(ResponseHeader header, Content body) {
        if (body == null) {
            body = new Content(new byte[0]);
        }
        this.body = body;
        this.header = header;
    }

    public byte[] getBytes() {
        return (header.toString() + body.toString()).getBytes();
    }
}
