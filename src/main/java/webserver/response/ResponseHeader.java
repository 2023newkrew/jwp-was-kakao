package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.content.Content;

public class ResponseHeader {
    private final String header;

    public ResponseHeader(HttpStatus httpStatus) {
        header = "HTTP/1.1 " + httpStatus + " \r\n"
                + "Content-Length: 0 \r\n"
                + "\r\n";
    }

    public ResponseHeader(HttpStatus httpStatus, Content body) {
        header = "HTTP/1.1 " + httpStatus + " \r\n"
                + "Content-Type: " + body.getContentType() + " \r\n"
                + "Content-Length: " + body.getContentLength() + " \r\n"
                + "\r\n";
    }

    @Override
    public String toString() {
        return header;
    }
}
