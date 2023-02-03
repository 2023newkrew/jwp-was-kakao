package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.view.View;

public class Response {

    private final HttpStatus httpStatus;

    private final View body;

    public Response(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public Response(HttpStatus httpStatus, View body) {

        if (body == null) {
            body = new View(new byte[0]);
        }
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public byte[] getBytes() {
        return (getHeader() + body.getContent()).getBytes();
    }

    private String getHeader() {
        return "HTTP/1.1 " + httpStatus + " \r\n"
                + "Content-Type: " + body.getContentType().getHeader() + " \r\n"
                + "Content-Length: " + body.getContentLength() + " \r\n"
                + "\r\n";
    }
}
