package webserver;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String uri;
    private final String httpVersion;
    private final Map<String, String> headers;


    public HttpRequest(HttpMethod httpMethod, String uri, String httpVersion, Map<String, String> headers) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }
}
