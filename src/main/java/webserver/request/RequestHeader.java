package webserver.request;

import org.springframework.http.HttpMethod;
import webserver.http.Cookie;
import webserver.http.header.HeaderType;
import webserver.http.header.Headers;

import java.util.List;
import java.util.Objects;

public class RequestHeader {

    private final RequestHead head;

    private final Headers headers;

    public RequestHeader(String head, List<String> headers) {
        this.head = new RequestHead(head);
        this.headers = new Headers(headers);
    }

    public HttpMethod getHttpMethod() {
        return head.getHttpMethod();
    }

    public String getPath() {
        return head.getPath();
    }

    public int getContentLength() {
        String contentLength = headers.get(HeaderType.CONTENT_LENGTH);
        if (Objects.isNull(contentLength)) {
            return 0;
        }

        return Integer.parseInt(contentLength);
    }

    public Cookie getCookie() {
        String cookie = headers.get(HeaderType.COOKIE);
        if (Objects.isNull(cookie)) {
            return null;
        }

        return new Cookie(cookie);
    }
}
