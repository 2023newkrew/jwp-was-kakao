package http.response;

import exception.InternalServerErrorException;
import http.ContentType;
import http.HttpResponseHeader;
import http.HttpStatus;

import java.util.Objects;

public class ResponseBuilder {
    private static final String NONE = "";
    private static final String SPACE = " ";
    private static final String COLON = ": ";
    private static final String NEW_LINE = " \r\n";
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
        String statusLine = makeStartLine();
        String headers = makeHeaders();
        byte[] body = makeBody();

        return new Response(statusLine, headers, body);
    }

    private String makeStartLine() {
        return version + SPACE + httpStatus.getCode() + SPACE + httpStatus.getMessage();
    }

    private String makeHeaders() {
        return String.join(
                NONE,
                Objects.isNull(contentType) ? NONE : makeOneHeader(HttpResponseHeader.CONTENT_TYPE, contentType.getValue()),
                Objects.isNull(contentLength) ? NONE : makeOneHeader(HttpResponseHeader.CONTENT_LENGTH, contentLength.toString()),
                Objects.isNull(connection) ? NONE : makeOneHeader(HttpResponseHeader.CONNECTION, connection),
                Objects.isNull(location) ? NONE : makeOneHeader(HttpResponseHeader.LOCATION, location)
        );
    }

    private String makeOneHeader(HttpResponseHeader httpResponseHeader, String value) {
        return httpResponseHeader + COLON + value + NEW_LINE;
    }

    private byte[] makeBody() {
        return Objects.isNull(this.body) ? new byte[]{} : this.body;
    }
}