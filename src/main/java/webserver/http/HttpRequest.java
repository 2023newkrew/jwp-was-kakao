package webserver.http;

import java.util.Map;

public class HttpRequest {
    private final String path;
    private final HttpMethod method;
    private final Map<String, String> parameter;

    public HttpRequest(final String path, final HttpMethod method, final Map<String, String> parameter) {
        this.path = path;
        this.method = method;
        this.parameter = parameter;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getParameter(String parameterName) {
        return parameter.get(parameterName);
    }
}
