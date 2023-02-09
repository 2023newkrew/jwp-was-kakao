package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.http.Content;
import webserver.http.Headers;

import java.util.Objects;

public class Response {
    private final HttpStatus httpStatus;

    private final Content body;

    private final Headers headers;

    public Response(HttpStatus httpStatus) {
        this(httpStatus, null, null);
    }

    public Response(HttpStatus httpStatus, Content body) {
        this(httpStatus, body, null);
    }

    public Response(HttpStatus httpStatus, Content body, Headers headers) {
        if (Objects.isNull(headers)) {
            headers = new Headers();
        }
        this.httpStatus = httpStatus;
        this.body = body;
        this.headers = headers;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Content getBody() {
        return body;
    }

    public Headers getHeaders() {
        return headers;
    }
}
