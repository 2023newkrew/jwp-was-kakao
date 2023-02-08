package webserver.request;

import java.util.Map;
import org.springframework.http.HttpMethod;
import webserver.FilenameExtension;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(HttpRequestLine httpRequestLine, Map<String, String> headers) {
        this(httpRequestLine, headers, null);
    }

    public HttpRequest(HttpRequestLine httpRequestLine, Map<String, String> headers, String body) {
        this.httpRequestLine = httpRequestLine;
        this.headers = headers;
        this.body = body;
    }

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getQueryParams() {
        return httpRequestLine.getQueryParams();
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getHttpMethod() {
        return httpRequestLine.getHttpMethod();
    }

    public String getPath() {
        return httpRequestLine.getPath();
    }

    public String getHttpVersion() {
        return httpRequestLine.getHttpVersion();
    }

    public FilenameExtension getFilenameExtension() {
        String path = getPath();
        String[] splitPath = path.split("\\.");
        String extension = splitPath[splitPath.length - 1];
        return FilenameExtension.from(extension);
    }
}
