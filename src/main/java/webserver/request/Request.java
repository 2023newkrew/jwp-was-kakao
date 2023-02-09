package webserver.request;

import org.springframework.http.HttpMethod;

public class Request {

    private final RequestHeader header;

    private final RequestBody body;

    public Request(RequestHeader header, RequestBody body) {
        this.header = header;
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return header.getHttpMethod();
    }

    public String getPath() {
        return header.getPath();
    }

    public String getBody(String key) {
        return body.get(key);
    }
}
