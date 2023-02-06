package http;

import java.net.URI;
import java.util.Optional;

public class HttpRequest {
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public Optional<String> getAttribute(HttpHeaders key) {
        return httpRequestHeader.getAttribute(key);
    }

    public String getMethod() {
        return httpRequestHeader.getMethod();
    }

    public URI getUri() {
        return httpRequestHeader.getUri();
    }

    public String getHttpVersion() {
        return httpRequestHeader.getHttpVersion();
    }

    public String  getBody() {
        return httpRequestBody.getContent();
    }

    public boolean checkStaticResource() {
        if(getUri().getPath().contains("/css")) return true;
        if(getUri().getPath().contains("/js")) return true;
        if(getUri().getPath().contains("/fonts")) return true;
        if(getUri().getPath().contains("/images")) return true;
        return false;
    }

    public boolean checkHtmlResource() {
        return getUri().getPath().endsWith(".html");
    }

    public boolean checkDynamic() {
        return !checkHtmlResource() && !checkStaticResource();
    }
}
