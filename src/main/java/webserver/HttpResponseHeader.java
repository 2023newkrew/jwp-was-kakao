package webserver;

import enums.ContentType;
import org.springframework.http.HttpStatus;

import java.util.List;

public class HttpResponseHeader {

    private List<String> headers;

    private HttpResponseHeader(List<String> headers) {
        this.headers = headers;
    }

    public static HttpResponseHeader of(HttpStatus status, ContentType contentType, int contentLength) {
        return new HttpResponseHeader(List.of(
                String.format("HTTP/1.1 %d %s", status.value(), status.name()),
                String.format("Content-Type: %s;charset=utf-8", contentType.getValue()),
                String.format("Content-Length: %s",contentLength)
        ));
    }

    public List<String> getHeaders() {
        return headers;
    }
}
