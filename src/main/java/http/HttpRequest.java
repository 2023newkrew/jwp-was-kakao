package http;

import java.net.URI;
import java.util.Optional;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestLine = httpRequestLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public Optional<String> getHeader(HttpHeaders key) {
        return httpRequestHeader.getAttribute(key);
    }

    public String getMethod() {
        return httpRequestLine.getMethod();
    }

    public URI getUri() {
        return httpRequestLine.getUri();
    }

    public String getPath() {
        return httpRequestLine.getUri().getPath();
    }

    public String getHttpVersion() {
        return httpRequestLine.getHttpVersion();
    }

    public String  getBody() {
        return httpRequestBody.getContent();
    }

    public boolean checkStaticResource() {
        if(getPath().contains("/css")) return true;
        if(getPath().contains("/js")) return true;
        if(getPath().contains("/fonts")) return true;
        if(getPath().contains("/images")) return true;
        return false;
    }

    public boolean checkHtmlResource() {
        return getPath().endsWith(".html");
    }

    public boolean checkDynamic() {
        return !checkHtmlResource() && !checkStaticResource();
    }
}
