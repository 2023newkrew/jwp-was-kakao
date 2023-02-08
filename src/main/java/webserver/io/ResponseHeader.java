package webserver.io;

import org.springframework.http.HttpStatus;
import webserver.http.Headers;
import webserver.http.content.Content;
import webserver.http.response.Response;

import java.util.Objects;

public class ResponseHeader {

    private final String header;

    private final Headers headers;

    public ResponseHeader(Response response) {
        this(response.getHttpStatus(), response.getBody(), response.getHeaders());
    }

    public ResponseHeader(HttpStatus httpStatus, Content body, Headers headers) {
        this.header = "HTTP/1.1 " + httpStatus + " \r\n";
        this.headers = headers;
        putHeader(body);
    }

    private void putHeader(Content body) {
        if (Objects.isNull(body)) {
            headers.put("Content-Length: 0");
            return;
        }
        headers.put("Content-Type: " + body.getContentType());
        headers.put("Content-Length: " + body.getContentLength());
    }

    public byte[] getBytes() {
        return (header + new String(headers.getBytes())).getBytes();
    }
}
