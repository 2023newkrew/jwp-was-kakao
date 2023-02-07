package webserver.http;

import java.util.Map;

public class HttpRequest {
    private final String path;
    private final HttpMethod method;
    private final HttpHeaders headers;
    private final Map<String, String> parameter;

    public HttpRequest(final String path, final HttpMethod method, final HttpHeaders headers, final Map<String, String> parameter) {
        this.path = path;
        this.method = method;
        this.headers = headers;
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

    /* 2단계에서 사용 예정 */
    @SuppressWarnings("unused")
    public String getHeader(String headerName) {
        return headers.getHeaders().get(headerName);
    }
}
