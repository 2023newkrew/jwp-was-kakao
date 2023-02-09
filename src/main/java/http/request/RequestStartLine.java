package http.request;

import http.HttpMethod;
import http.Uri;
import utils.RequestParsingUtils;

public class RequestStartLine {
    private HttpMethod method;
    private Uri uri;
    private String version;

    public RequestStartLine(HttpMethod method, Uri uri, String version) {
        this.method = method;
        this.uri = uri;
        this.version = version;
    }

    public static RequestStartLine fromRawStartLine(String rawStartLine) {
        return RequestParsingUtils.parseStartLine(rawStartLine);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Uri getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }
}
