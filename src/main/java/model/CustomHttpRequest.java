package model;

import java.util.Map;

public class CustomHttpRequest {

    private final CustomHttpMethod customHttpMethod;
    private final String url;
    private final Map<String, String> query;
    private final String protocol;
    private final Map<String, String> headers;
    private final String body;

    public CustomHttpRequest(CustomHttpMethod customHttpMethod, String url, Map<String, String> query, String protocol, Map<String, String> headers, String body) {
        this.customHttpMethod = customHttpMethod;
        this.url = url;
        this.query = query;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public CustomHttpMethod getHttpMethod() {
        return customHttpMethod;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
