package webserver.response;

import org.springframework.http.HttpStatus;

public class Response {

    private final HttpStatus httpStatus;

    private final byte[] body;

    private final MediaType contentType;

    public Response(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public Response(HttpStatus httpStatus, byte[] body) {
        this(httpStatus, body, MediaType.TEXT_HTML);
    }

    public Response(HttpStatus httpStatus, byte[] body, MediaType contentType) {
        if (body == null) {
            body = new byte[0];
        }
        this.httpStatus = httpStatus;
        this.body = body;
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return (getHeader() + new String(body)).getBytes();
    }

    private String getHeader() {
        return "HTTP/1.1 " + httpStatus + " \r\n"
                + "Content-Type: " + contentType.getHeader() + " \r\n"
                + "Content-Length: " + body.length + " \r\n"
                + "\r\n";
    }
}
