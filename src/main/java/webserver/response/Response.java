package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.content.Content;

public class Response {

    private final HttpStatus httpStatus;

    private final Content body;

    public Response(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public Response(HttpStatus httpStatus, Content body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Content getBody() {
        return body;
    }

    public byte[] getBytes() {
        return body.getBytes();
    }
}
