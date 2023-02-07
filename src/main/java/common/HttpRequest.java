package common;

import common.HttpMethod;

import java.util.Map;

public class HttpRequest {
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameter;

    public HttpRequest(final String uri, final HttpMethod method, final Map<String, String> parameter) {
        this.uri = uri;
        this.method = method;
        this.parameter = parameter;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }
}
