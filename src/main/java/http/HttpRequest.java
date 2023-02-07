package http;

import java.net.URI;
import java.util.Objects;
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

    public Optional<String> getParam(String key) {
        if(httpRequestLine.getMethod().equals("GET"))
            return httpRequestLine.getParam(key);
        if(httpRequestLine.getMethod().equals("POST"))
            return httpRequestBody.getParam(key);
        return Optional.empty();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest request = (HttpRequest) o;
        return Objects.equals(httpRequestLine, request.httpRequestLine) && Objects.equals(httpRequestHeader, request.httpRequestHeader) && Objects.equals(httpRequestBody, request.httpRequestBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpRequestLine, httpRequestHeader, httpRequestBody);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpRequestLine=" + httpRequestLine +
                ", httpRequestHeader=" + httpRequestHeader +
                ", httpRequestBody=" + httpRequestBody +
                '}';
    }
}
