package webserver;

import java.util.Map;
import lombok.Builder;

@Builder
public class RequestHeader {

    private HttpMethod httpMethod;

    private String url;

    private Map<String, String> queryParams;

    private String httpVersion;

    private Map<String, String> headers;

    public RequestHeader(HttpMethod httpMethod, String url, Map<String, String> queryParams, String httpVersion,
        Map<String, String> headers) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryParams = queryParams;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void convertToAbsolutePath(ResourceType resourceType) {
        url = resourceType.getPath() + url;
    }

    public boolean hasContentLength() {
        return headers.containsKey("Content-Length");
    }
}
