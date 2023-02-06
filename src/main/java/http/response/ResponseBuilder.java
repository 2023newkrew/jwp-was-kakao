package http.response;

import http.ContentType;
import http.HttpStatus;

import java.util.Objects;

public class ResponseBuilder {
    private HttpStatus httpStatus;
    private ContentType contentType;
    private Integer contentLength;
    private String connection;
    private String location;

    private byte[] body;

    public ResponseBuilder() {
    }

    public ResponseBuilder httpStatus(HttpStatus status) {
        this.httpStatus = status;
        return this;
    }

    public ResponseBuilder contentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public ResponseBuilder contentLength(Integer contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public ResponseBuilder connection(String connection) {
        this.connection = connection;
        return this;
    }

    public ResponseBuilder location(String location) {
        this.location = location;
        return this;
    }

    public ResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public Response build() {
        String statusLine = "HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getMessage();
        String headers = (contentType != null ? ("Content-Type: " + contentType.getValue() + " \r\n") : "")
                + (contentLength != null ? ("Content-Length: " + contentLength + " \r\n") : "")
                + (connection != null ? ("Connection: " + connection + " \r\n") : "")
                + (location != null ? ("Location: " + location + " \r\n") : "");
        byte[] body = Objects.isNull(this.body) ? new byte[]{} : this.body;

        return new Response(statusLine, headers, body);
    }
}