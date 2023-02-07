package request;

import utils.ContentType;

public class RequestParams {
    private final String method;
    private final String url;
    private final String httpVersion;
    private final ContentType contentType;

    public RequestParams(String method, String url, String httpVersion, ContentType contentType) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
        this.contentType = contentType;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
