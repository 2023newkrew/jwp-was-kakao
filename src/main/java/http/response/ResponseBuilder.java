package http.response;

import exception.InternalServerErrorException;
import http.ContentType;
import http.HttpStatus;

import java.util.Objects;

public class ResponseBuilder {
    private String version;
    private HttpStatus httpStatus;
    private ContentType contentType;
    private Integer contentLength;
    private String connection;
    private String location;

    private byte[] body;

    public ResponseBuilder() {
    }

    public ResponseBuilder httpVersion(String version) {
        this.version = version;
        return this;
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
        if (Objects.isNull(version) || Objects.isNull(httpStatus)) {
            throw new InternalServerErrorException("http version 과 httpStatus 는 반드시 입력되어야 합니다.");
        }
        String statusLine = version + " " + httpStatus.getCode() + " " + httpStatus.getMessage();
        String headers = (contentType != null ? ("Content-Type: " + contentType.getValue() + " \r\n") : "")
                + (contentLength != null ? ("Content-Length: " + contentLength + " \r\n") : "")
                + (connection != null ? ("Connection: " + connection + " \r\n") : "")
                + (location != null ? ("Location: " + location + " \r\n") : "");
        byte[] body = Objects.isNull(this.body) ? new byte[]{} : this.body;

        return new Response(statusLine, headers, body);
    }
}