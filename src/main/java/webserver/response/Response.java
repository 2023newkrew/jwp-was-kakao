package webserver.response;

import java.util.Objects;

public class Response {

    private final ResponseHeader header;

    private final ResponseBody body;

    public Response(ResponseHeader header) {
        this(header, null);
    }

    public Response(ResponseHeader header, ResponseBody body) {
        this.header = header;
        this.body = body;
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public ResponseBody getBody() {
        return body;
    }

    public byte[] getBytes() {
        return toString().getBytes();
    }

    @Override
    public String toString() {
        if (Objects.isNull(body)) {
            return header.toString();
        }

        return header.toString() + body;
    }
}
