package common;

import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private Map<String, String> cookies;

    public HttpRequest(final String uri, final HttpMethod method, final Map<String, String> headers, final Map<String, String> parameters, final Map<String, String> cookies) {
        this.uri = uri;
        this.method = method;
        this.parameters = parameters;
        this.headers = headers;
        this.cookies = cookies;
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Optional<String> getHeader(String key) {return Optional.ofNullable(headers.get(key)); }

    public Optional<String> getCookie(String name) {
        return Optional.ofNullable(cookies.get(name));
    }
}
