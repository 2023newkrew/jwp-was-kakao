package webserver;

public class Response {

    private final String header;

    private final String body;

    public Response(String header, String body) {
        this.header = header +
        "Content-Length: " + body.getBytes().length + " ";
        this.body = body;
    }

    public Response(String header) {
        this.header = header +
                "Content-Length: 0 ";
        this.body = null;
    }

    public byte[] toBytes() {
        if (body == null) {
            return header.getBytes();
        }
        return (header + "\r\n\r\n" + body).getBytes();
    }

    public String getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }
}
