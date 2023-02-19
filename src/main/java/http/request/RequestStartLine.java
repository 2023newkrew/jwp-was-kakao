package http.request;

import exception.BadRequestException;
import http.HttpMethod;
import http.Uri;
import utils.RequestParsingUtils;

import java.util.Objects;

public class RequestStartLine {
    private final HttpMethod method;
    private final Uri uri;
    private final String version;

    public RequestStartLine(HttpMethod method, Uri uri, String version) {
        if (Objects.isNull(method) || Objects.isNull(uri) || Objects.isNull(version)) {
            throw new BadRequestException();
        }
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
