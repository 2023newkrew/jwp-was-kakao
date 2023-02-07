package webserver.io;

import org.springframework.http.HttpStatus;
import webserver.content.Content;
import webserver.response.Response;

import java.util.Objects;

public class ResponseHeader {

    private final String header;

    public ResponseHeader(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public ResponseHeader(Response response) {
        this(response.getHttpStatus(), response.getBody());
    }

    public ResponseHeader(HttpStatus httpStatus, Content body) {
        header = createHeader(httpStatus, body);
    }

    private static String createHeader(HttpStatus httpStatus, Content body) {
        if (Objects.isNull(body)) {
            return "HTTP/1.1 " + httpStatus + " \r\n"
                    + "Content-Length: 0 \r\n"
                    + "\r\n";
        }
        return "HTTP/1.1 " + httpStatus + " \r\n"
                + "Content-Type: " + body.getContentType() + " \r\n"
                + "Content-Length: " + body.getContentLength() + " \r\n"
                + "\r\n";
    }

    public byte[] getBytes() {
        return header.getBytes();
    }
}
