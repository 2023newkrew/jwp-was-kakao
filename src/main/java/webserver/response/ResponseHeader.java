package webserver.response;

import org.springframework.http.HttpStatus;
import webserver.http.header.HeaderType;
import webserver.http.header.Headers;

import java.util.Objects;

public class ResponseHeader {

    private final ResponseHead head;

    private final Headers headers;

    public ResponseHeader(HttpStatus httpStatus, Headers headers) {
        this(httpStatus, headers, null);
    }

    public ResponseHeader(HttpStatus httpStatus, ResponseBody responseBody) {
        this(httpStatus, new Headers(), responseBody);
    }

    public ResponseHeader(HttpStatus httpStatus, Headers headers, ResponseBody responseBody) {
        if (Objects.isNull(headers)) {
            headers = new Headers();
        }
        this.head = new ResponseHead(httpStatus);
        this.headers = headers;
        putContentHeader(responseBody);
    }

    private void putContentHeader(ResponseBody body) {
        int contentLength = 0;
        if (Objects.nonNull(body)) {
            headers.put(HeaderType.CONTENT_TYPE, body.getContentType());
            contentLength = body.getContentLength();
        }
        headers.put(HeaderType.CONTENT_LENGTH, contentLength);
    }

    @Override
    public String toString() {
        return head.toString() + headers.toString();
    }
}
